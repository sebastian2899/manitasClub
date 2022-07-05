import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AcudienteService } from '../service/acudiente.service';

import { AcudienteComponent } from './acudiente.component';

describe('Acudiente Management Component', () => {
  let comp: AcudienteComponent;
  let fixture: ComponentFixture<AcudienteComponent>;
  let service: AcudienteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AcudienteComponent],
    })
      .overrideTemplate(AcudienteComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AcudienteComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AcudienteService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.acudientes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
