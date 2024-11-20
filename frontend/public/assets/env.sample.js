(function (window) {
    window['env'] = window['env'] || {};
    window['env']['backendHost'] = '${BACKEND_HOST}';
    window['env']['backendPort'] = '${BACKEND_PORT}';
    window['env']['backendProtocolSchema'] = '${BACKEND_PROTOCOL_SCHEMA}';
    window['env']['backendModeratorPort'] = '${BACKEND_MODERATOR_PORT}';
})(this);