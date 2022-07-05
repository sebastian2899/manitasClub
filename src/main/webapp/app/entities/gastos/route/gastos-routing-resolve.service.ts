import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGastos, Gastos } from '../gastos.model';
import { GastosService } from '../service/gastos.service';

@Injectable({ providedIn: 'root' })
export class GastosRoutingResolveService implements Resolve<IGastos> {
  constructor(protected service: GastosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGastos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((gastos: HttpResponse<Gastos>) => {
          if (gastos.body) {
            return of(gastos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Gastos());
  }
}
