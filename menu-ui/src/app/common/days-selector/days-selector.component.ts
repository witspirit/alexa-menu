import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-days-selector',
  templateUrl: './days-selector.component.html',
  styleUrls: ['./days-selector.component.scss']
})
export class DaysSelectorComponent implements OnInit, OnChanges {
  @Input() nrOfDays: number; // in days

  @Output() onUpdate = new EventEmitter<number>();

  selectedNrOfDays = new FormControl();

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.selectedNrOfDays.setValue(this.nrOfDays);
  }

  public applyUpdate() {
    this.nrOfDays = this.selectedNrOfDays.value;
    this.onUpdate.emit(this.nrOfDays);
  }

}
