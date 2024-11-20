export const environment = {
    production: false,
    backendHost: 
        window[<any>'env'][<any>'backendHost'] || 'localhost',
    backendPort: 
        window[<any>'env'][<any>'backendPort'] || 4200,
    backendProtocolSchema: 
        window[<any>'env'][<any>'backendProtocolSchema'] || 'http',
    backendModeratorPort:  
        window[<any>'env'][<any>'backendModeratorPort'] || 8040
};