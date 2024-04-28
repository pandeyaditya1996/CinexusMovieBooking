import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfessorsComponent } from './professors.component';

describe('ProfessorsComponent', () => {
  let component: ProfessorsComponent;
  let fixture: ComponentFixture<ProfessorsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfessorsComponent]
    });
    fixture = TestBed.createComponent(ProfessorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
