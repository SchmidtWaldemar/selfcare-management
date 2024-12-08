(function (window) {
    window['env'] = window['env'] || {};
    window['env']['backendHost'] = '${BACKEND_HOST}';
    window['env']['backendPort'] = '${BACKEND_PORT}';
    window['env']['backendProtocolSchema'] = '${BACKEND_PROTOCOL_SCHEMA}';
    window['env']['backendModeratorPort'] = '${BACKEND_MODERATOR_PORT}';
    window['env']['backendKeycloakHost'] = '${BACKEND_KEYCLOAK_HOST}';
    window['env']['backendKeycloakPort'] = '${BACKEND_KEYCLOAK_PORT}';
})(this);