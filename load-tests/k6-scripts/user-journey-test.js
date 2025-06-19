import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

export let errorRate = new Rate('errors');

export let options = {
  vus: 10,
  duration: '5m',
  thresholds: {
    http_req_duration: ['p(95)<1000'],
    errors: ['rate<0.1'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  let userId;
  
  let listResponse = http.get(`${BASE_URL}/api/users`);
  check(listResponse, {
    'list users status is 200 or 429': (r) => r.status === 200 || r.status === 429,
  });
  
  if (listResponse.status !== 429) {
    let createResponse = http.post(`${BASE_URL}/api/users`, JSON.stringify({
      name: `Test User ${__VU}-${__ITER}`,
      email: `testuser${__VU}${__ITER}@example.com`
    }), {
      headers: { 'Content-Type': 'application/json' },
    });
    
    let createResult = check(createResponse, {
      'create user status is 201 or 429': (r) => r.status === 201 || r.status === 429,
    });
    
    if (createResponse.status === 201) {
      userId = JSON.parse(createResponse.body).id;
      
      let getResponse = http.get(`${BASE_URL}/api/users/${userId}`);
      check(getResponse, {
        'get user status is 200 or 429': (r) => r.status === 200 || r.status === 429,
      });
      
      let updateResponse = http.put(`${BASE_URL}/api/users/${userId}`, JSON.stringify({
        name: `Updated User ${__VU}-${__ITER}`,
        email: `updated${__VU}${__ITER}@example.com`
      }), {
        headers: { 'Content-Type': 'application/json' },
      });
      
      check(updateResponse, {
        'update user status is 200 or 429': (r) => r.status === 200 || r.status === 429,
      });
      
      let deleteResponse = http.del(`${BASE_URL}/api/users/${userId}`);
      check(deleteResponse, {
        'delete user status is 204 or 429': (r) => r.status === 204 || r.status === 429,
      });
    }
    
    errorRate.add(!createResult);
  }
  
  sleep(1);
}
