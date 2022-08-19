import { waitForAsync, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResetSubmissionComponent } from './reset-submission.component';

describe('ResetSubmissionComponent', () => {
  let component: ResetSubmissionComponent;
  let fixture: ComponentFixture<ResetSubmissionComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ ResetSubmissionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResetSubmissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
