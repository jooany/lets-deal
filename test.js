import http from 'k6/http';
import { check } from 'k6';

export const options = {
  vus: 50,
  duration: '30s',
};

export default function () {
  const saleId = 1;
  const url = `http://localhost:9898/api/v1/sales/${saleId}/proposals-async`;

  const payload = JSON.stringify({
    buyerPrice: 10000
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyTmFtZSI6ImJyaWdodCIsImlhdCI6MTc1OTU3NDA3NiwiZXhwIjoxNzYyMTY2MDc2fQ.7MJSpdYwRcJCDZW2F4XmUuqor2nLi5jdUtrYKrydPFw',
    },
  };

  const res = http.post(url, payload, params);
  check(res, { 'is status 200': (r) => r.status === 200 });
}