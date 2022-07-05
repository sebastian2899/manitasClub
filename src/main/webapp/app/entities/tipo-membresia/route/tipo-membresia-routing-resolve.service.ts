import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoMembresia, TipoMembresia } from '../tipo-membresia.model';
import { TipoMembresiaService } from '../service/tipo-membresia.service';

@Injectable({ providedIn: 'root' })
export class TipoMembresiaRoutingResolveService implements Resolve<ITipoMembresia> {
  constructor(protected service: TipoMembresiaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoMembresia> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoMembresia: HttpResponse<TipoMembresia>) => {
          if (tipoMembresia.body) {
            return of(tipoMembresia.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoMembresia());
  }
}
