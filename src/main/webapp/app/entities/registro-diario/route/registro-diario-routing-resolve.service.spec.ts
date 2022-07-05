import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRegistroDiario, RegistroDiario } from '../registro-diario.model';
import { RegistroDiarioService } from '../service/registro-diario.service';

import { RegistroDiarioRoutingResolveService } from './registro-diario-routing-resolve.service';

describe('RegistroDiario routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RegistroDiarioRoutingResolveService;
  let service: RegistroDiarioService;
  let resultRegistroDiario: IRegistroDiario | undefined;

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
    routingResolveService = TestBed.inject(RegistroDiarioRoutingResolveService);
    service = TestBed.inject(RegistroDiarioService);
    resultRegistroDiario = undefined;
  });

  describe('resolve', () => {
    it('should return IRegistroDiario returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRegistroDiario = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRegistroDiario).toEqual({ id: 123 });
    });

    it('should return new IRegistroDiario if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRegistroDiario = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRegistroDiario).toEqual(new RegistroDiario());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RegistroDiario })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRegistroDiario = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRegistroDiario).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
