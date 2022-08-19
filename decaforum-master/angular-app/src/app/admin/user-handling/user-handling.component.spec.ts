import { waitForAsync, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserHandlingComponent } from './user-handling.component';

describe('UserHandlingComponent', () => {
  let component: UserHandlingComponent;
  let fixture: ComponentFixture<UserHandlingComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ UserHandlingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserHandlingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
