import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITotalGastos, TotalGastos } from '../total-gastos.model';
import { TotalGastosService } from '../service/total-gastos.service';

@Injectable({ providedIn: 'root' })
export class TotalGastosRoutingResolveService implements Resolve<ITotalGastos> {
  constructor(protected service: TotalGastosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITotalGastos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((totalGastos: HttpResponse<TotalGastos>) => {
          if (totalGastos.body) {
            return of(totalGastos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TotalGastos());
  }
}
