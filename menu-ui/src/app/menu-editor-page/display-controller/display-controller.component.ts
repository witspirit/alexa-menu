import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import * as moment from 'moment';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-display-controller',
  templateUrl: './display-controller.component.html',
  styleUrls: ['./display-controller.component.scss']
})
export class DisplayControllerComponent implements OnInit, OnChanges {
  @Input() date: moment.Moment;
  @Input() jumpLength: number; // in days
  @Input() nrOfDays: number; // in days

  @Output() onStartDateUpdate = new EventEmitter<moment.Moment>();
  @Output() onNrOfDaysUpdate = new EventEmitter<number>();

  selectedNrOfDays = new FormControl();

  constructor() {
  }

  ngOnInit() {
  }

  public applyNrOfDaysUpdate() {
    this.nrOfDays = this.selectedNrOfDays.value;
    this.onNrOfDaysUpdate.emit(this.nrOfDays);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.selectedNrOfDays.setValue(this.nrOfDays);
  }

  private update(desiredDate: moment.Moment) {
    this.onStartDateUpdate.emit(desiredDate);
  }

}
