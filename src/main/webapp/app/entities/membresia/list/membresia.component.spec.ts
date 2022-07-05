import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MembresiaService } from '../service/membresia.service';

import { MembresiaComponent } from './membresia.component';

describe('Membresia Management Component', () => {
  let comp: MembresiaComponent;
  let fixture: ComponentFixture<MembresiaComponent>;
  let service: MembresiaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MembresiaComponent],
    })
      .overrideTemplate(MembresiaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MembresiaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(MembresiaService);

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
    expect(comp.membresias?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
