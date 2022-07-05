import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITotalGastos, TotalGastos } from '../total-gastos.model';
import { TotalGastosService } from '../service/total-gastos.service';

import { TotalGastosRoutingResolveService } from './total-gastos-routing-resolve.service';

describe('TotalGastos routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TotalGastosRoutingResolveService;
  let service: TotalGastosService;
  let resultTotalGastos: ITotalGastos | undefined;

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
    routingResolveService = TestBed.inject(TotalGastosRoutingResolveService);
    service = TestBed.inject(TotalGastosService);
    resultTotalGastos = undefined;
  });

  describe('resolve', () => {
    it('should return ITotalGastos returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTotalGastos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTotalGastos).toEqual({ id: 123 });
    });

    it('should return new ITotalGastos if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTotalGastos = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTotalGastos).toEqual(new TotalGastos());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TotalGastos })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTotalGastos = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTotalGastos).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
