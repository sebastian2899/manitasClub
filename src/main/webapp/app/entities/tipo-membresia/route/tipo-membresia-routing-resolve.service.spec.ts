import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITipoMembresia, TipoMembresia } from '../tipo-membresia.model';
import { TipoMembresiaService } from '../service/tipo-membresia.service';

import { TipoMembresiaRoutingResolveService } from './tipo-membresia-routing-resolve.service';

describe('TipoMembresia routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TipoMembresiaRoutingResolveService;
  let service: TipoMembresiaService;
  let resultTipoMembresia: ITipoMembresia | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TipoMembresiaRoutingResolveService);
    service = TestBed.inject(TipoMembresiaService);
    resultTipoMembresia = undefined;
  });

  describe('resolve', () => {
    it('should return ITipoMembresia returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoMembresia = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoMembresia).toEqual({ id: 123 });
    });

    it('should return new ITipoMembresia if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoMembresia = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTipoMembresia).toEqual(new TipoMembresia());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TipoMembresia })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTipoMembresia = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTipoMembresia).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
