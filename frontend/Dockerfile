FROM node:18 as build

#RUN npm install -g @angular/cli

COPY  package.json package-lock.json /app/
COPY  src /app/
COPY  keycloak /app/

WORKDIR /app

RUN npm install

COPY . .

#EXPOSE 4200
#CMD ng serve --host 0.0.0.0 --disable-host-check

RUN npm run build --prod

FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 80