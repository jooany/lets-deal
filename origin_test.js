import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 50,
  duration: '30s',
};

export default function () {
  const saleId = 1;
  const url = `http://localhost:9898/api/v1/sales/${saleId}/proposals`;

  const payload = JSON.stringify({
    buyerPrice: 10000
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6ImhhcHB5IiwiaWF0IjoxNzU5NTY5OTM0LCJleHAiOjE3NjIxNjE5MzR9.xJPBq0BUm1lCMENxkaMG8-qf22ywgVv6vyFdFDQIsSk',
    },
  };

  const res = http.post(url, payload, params);
  check(res, { 'is status 200': (r) => r.status === 200 });
}