import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import * as moment from 'moment';

@Component({
  selector: 'app-display-controller',
  templateUrl: './display-controller.component.html',
  styleUrls: ['./display-controller.component.scss']
})
export class DisplayControllerComponent implements OnInit {
  @Input() date: moment.Moment;
  @Input() jumpLength: number; // in days
  @Input() nrOfDays: number; // in days

  @Output() onStartDateUpdate = new EventEmitter<moment.Moment>();
  @Output() onNrOfDaysUpdate = new EventEmitter<number>();

  constructor() {
  }

  ngOnInit() {
  }

  private updateStartDate(desiredDate: moment.Moment) {
    this.onStartDateUpdate.emit(desiredDate);
  }

  private updateNrOfDays(desiredNrOfDays: number) {
    this.onNrOfDaysUpdate.emit(desiredNrOfDays);
  }

}
