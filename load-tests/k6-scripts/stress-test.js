import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

export let errorRate = new Rate('errors');

export let options = {
  stages: [
    { duration: '2m', target: 50 },
    { duration: '5m', target: 100 },
    { duration: '2m', target: 200 },
    { duration: '5m', target: 200 },
    { duration: '2m', target: 100 },
    { duration: '2m', target: 50 },
    { duration: '2m', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<1000'],
    errors: ['rate<0.5'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

const scenarios = [
  () => http.get(`${BASE_URL}/api/users`),
  () => http.get(`${BASE_URL}/api/users/1`),
  () => http.post(`${BASE_URL}/api/users`, JSON.stringify({
    name: `User ${Math.random()}`,
    email: `user${Math.random()}@example.com`
  }), {
    headers: { 'Content-Type': 'application/json' },
  }),
];

export default function () {
  let scenario = scenarios[Math.floor(Math.random() * scenarios.length)];
  let response = scenario();
  
  let result = check(response, {
    'status is not 5xx': (r) => r.status < 500,
    'response time < 1000ms': (r) => r.timings.duration < 1000,
  });
  
  errorRate.add(!result);
  
  sleep(Math.random() * 2);
}
