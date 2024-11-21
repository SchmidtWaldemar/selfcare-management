export const environment = {
    production: false,
    backendHost: 
        window[<any>'env'][<any>'backendHost'] || 'localhost',
    backendPort: 
        window[<any>'env'][<any>'backendPort'] || 8222,
    backendProtocolSchema: 
        window[<any>'env'][<any>'backendProtocolSchema'] || 'http',
    backendModeratorPort:  
        window[<any>'env'][<any>'backendModeratorPort'] || 8040,
    backendKeycloakPort:  
        window[<any>'env'][<any>'backendKeycloakPort'] || 7080    
};