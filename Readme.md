### A demo on how to log with MDC context in reactive spring-boot application.

* Populate reactive context in handler filter
* Retrieve reactive context in handler function and set thread' MDC before each log statement.
* Set log pattern with %mdc in application.properties

##### Log message Samples -
```
curl -X GET 'http://localhost:8080/hello?name=zaid' \
  -H 'x-request-id: test-request-id'

2021-05-30 18:51:47.119 INFO  [reactor-http-nio-4] className=c.z.s.w.l.h.HelloHandler <x-request-id=test-request-id, User-Agent=curl/7.64.1, Host=localhost:8080, sessionId=cc122dca-ebd1-4619-9a1c-90194e1e7834, Accept=*/*> Serving request
```

```
curl -X GET 'http://localhost:8080/hello-again?name=zaid' \
  -H 'x-request-id: test-request-id'

2021-05-30 19:02:11.709 INFO  [reactor-http-nio-5] className=c.z.s.w.l.h.HelloHandler <x-request-id=test-request-id, User-Agent=curl/7.64.1, Host=localhost:8080, sessionId=1a1a64c0-353a-41d0-b5c6-a9a08a074981, Accept=*/*> Response status code 200 OK
```

```
curl -X GET 'http://localhost:8080/hello-failure?name=zaid' \
  -H 'x-request-id: test-request-id'

2021-05-30 19:04:47.716 ERROR [reactor-http-nio-7] className=c.z.s.w.l.h.HelloHandler <x-request-id=test-request-id, User-Agent=curl/7.64.1, Host=localhost:8080, sessionId=f2619dbe-d689-4e64-83f5-73c0ef52fd07, Accept=*/*> Failed to process request
```