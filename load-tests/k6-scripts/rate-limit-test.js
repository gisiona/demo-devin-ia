import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

export let errorRate = new Rate('errors');
export let rateLimitRate = new Rate('rate_limits');

export let options = {
  vus: 1,
  duration: '2m',
  thresholds: {
    rate_limits: ['rate>0'],
    http_req_duration: ['p(95)<1000'],
  },
};

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

export default function () {
  for (let i = 0; i < 25; i++) {
    let response = http.get(`${BASE_URL}/api/users`);
    
    let result = check(response, {
      'status is 200 or 429': (r) => r.status === 200 || r.status === 429,
      'rate limit triggered': (r) => r.status === 429,
    });
    
    if (response.status === 429) {
      rateLimitRate.add(1);
      console.log(`Rate limit triggered after ${i + 1} requests`);
      break;
    } else {
      rateLimitRate.add(0);
    }
    
    errorRate.add(!result);
    sleep(0.1);
  }
  
  sleep(60);
}
