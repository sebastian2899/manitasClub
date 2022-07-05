import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAcudiente, Acudiente } from '../acudiente.model';
import { AcudienteService } from '../service/acudiente.service';

@Injectable({ providedIn: 'root' })
export class AcudienteRoutingResolveService implements Resolve<IAcudiente> {
  constructor(protected service: AcudienteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAcudiente> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((acudiente: HttpResponse<Acudiente>) => {
          if (acudiente.body) {
            return of(acudiente.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Acudiente());
  }
}
