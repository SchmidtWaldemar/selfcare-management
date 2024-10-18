import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
    
    constructor() {}
    
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        
        const token = localStorage.getItem('token');
        if (token) {
            const request = req.clone({
                headers: new HttpHeaders({
                    Authorization: `Bearer ${token}`
                })
            });
            return next.handle(request);
        }
        return next.handle(req);
    }
}
