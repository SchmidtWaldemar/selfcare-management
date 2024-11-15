FROM node:18

RUN npm install -g @angular/cli

COPY  package.json package-lock.json /app/
COPY  src /app/
COPY  keycloak /app/

WORKDIR /app

RUN npm install

COPY . .

EXPOSE 4200
CMD ng serve --host 0.0.0.0 --disable-host-check