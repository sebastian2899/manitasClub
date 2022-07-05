import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMembresia, Membresia } from '../membresia.model';
import { MembresiaService } from '../service/membresia.service';

@Injectable({ providedIn: 'root' })
export class MembresiaRoutingResolveService implements Resolve<IMembresia> {
  constructor(protected service: MembresiaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMembresia> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((membresia: HttpResponse<Membresia>) => {
          if (membresia.body) {
            return of(membresia.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Membresia());
  }
}
