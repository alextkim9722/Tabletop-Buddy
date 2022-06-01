FROM node:16-alpine as builder

WORKDIR /home/app
COPY . .
RUN npm ci && npm cache clean --force

RUN npm run build

FROM nginx:1.21-alpine

ENV TABLETOPBUDDY_ROOT_URL="http://localhost:8080/api"
COPY --from=builder /home/app/build /usr/share/nginx/html
COPY 85-set-environment-vars.sh /docker-entrypoint.d
RUN chmod +x /docker-entrypoint.d/85-set-environment-vars.sh

COPY nginx.conf /etc/nginx/conf.d/default.conf


EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]