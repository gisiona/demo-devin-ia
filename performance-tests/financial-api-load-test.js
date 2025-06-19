import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend, Counter } from 'k6/metrics';

export let errorRate = new Rate('errors');
export let responseTime = new Trend('response_time');
export let requestsPerSecond = new Counter('requests_per_second');

export let options = {
  stages: [
    { duration: '2m', target: 10 }, // Ramp up to 10 users
    { duration: '5m', target: 10 }, // Stay at 10 users
    { duration: '2m', target: 20 }, // Ramp up to 20 users
    { duration: '5m', target: 20 }, // Stay at 20 users
    { duration: '2m', target: 0 },  // Ramp down to 0 users
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% of requests must complete below 500ms
    http_req_failed: ['rate<0.1'],    // Error rate must be below 10%
  },
};

const BASE_URL = 'http://localhost:8080';

const testUsers = [
  { email: 'user1@test.com', password: 'password123', firstName: 'User', lastName: 'One', cpf: '11111111111' },
  { email: 'user2@test.com', password: 'password123', firstName: 'User', lastName: 'Two', cpf: '22222222222' },
  { email: 'user3@test.com', password: 'password123', firstName: 'User', lastName: 'Three', cpf: '33333333333' },
];

let authTokens = [];

export function setup() {
  console.log('Setting up test users...');
  
  testUsers.forEach((user, index) => {
    let registerResponse = http.post(`${BASE_URL}/api/v1/users/register`, JSON.stringify(user), {
      headers: { 'Content-Type': 'application/json' },
    });
    
    if (registerResponse.status === 201 || registerResponse.status === 400) {
      let loginResponse = http.post(`${BASE_URL}/api/v1/users/login`, JSON.stringify({
        email: user.email,
        password: user.password
      }), {
        headers: { 'Content-Type': 'application/json' },
      });
      
      if (loginResponse.status === 200) {
        let loginData = JSON.parse(loginResponse.body);
        authTokens.push(loginData.token);
        console.log(`User ${index + 1} authenticated successfully`);
      }
    }
  });
  
  return { tokens: authTokens };
}

export default function(data) {
  let token = data.tokens[Math.floor(Math.random() * data.tokens.length)];
  
  if (!token) {
    console.log('No valid token available, skipping iteration');
    return;
  }
  
  let headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  };
  
  let profileResponse = http.get(`${BASE_URL}/api/v1/users/profile`, { headers });
  check(profileResponse, {
    'profile status is 200': (r) => r.status === 200,
    'profile response time < 500ms': (r) => r.timings.duration < 500,
  });
  
  errorRate.add(profileResponse.status !== 200);
  responseTime.add(profileResponse.timings.duration);
  requestsPerSecond.add(1);
  
  sleep(1);
  
  let usersListResponse = http.get(`${BASE_URL}/api/v1/users`, { headers });
  check(usersListResponse, {
    'users list status is 200': (r) => r.status === 200,
    'users list response time < 500ms': (r) => r.timings.duration < 500,
  });
  
  errorRate.add(usersListResponse.status !== 200);
  responseTime.add(usersListResponse.timings.duration);
  requestsPerSecond.add(1);
  
  sleep(1);
  
  let userByIdResponse = http.get(`${BASE_URL}/api/v1/users/1`, { headers });
  check(userByIdResponse, {
    'user by id status is 200 or 404': (r) => r.status === 200 || r.status === 404,
    'user by id response time < 500ms': (r) => r.timings.duration < 500,
  });
  
  errorRate.add(userByIdResponse.status !== 200 && userByIdResponse.status !== 404);
  responseTime.add(userByIdResponse.timings.duration);
  requestsPerSecond.add(1);
  
  sleep(2);
}

export function teardown(data) {
  console.log('Performance test completed');
  console.log(`Total authenticated users: ${data.tokens.length}`);
}
