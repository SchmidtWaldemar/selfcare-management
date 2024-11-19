FROM node:18 as build

COPY  package.json package-lock.json /app/
COPY  src /app/
COPY  keycloak /app/

WORKDIR /app

RUN npm install

COPY . .

RUN npm run build --prod

FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 8080