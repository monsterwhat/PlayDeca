import { Tabs } from '@vizuaalog/bulmajs/src/plugins/tabs.js';
import { Component, OnInit } from '@angular/core';
import { Bulma } from '@vizuaalog/bulmajs/src/core';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class InfoComponent implements OnInit {

  constructor() { }

  private _targetElement = 'Rules';
  public get targetElement() {
    return this._targetElement;
  }
  public set targetElement(value) {
    this._targetElement = value;
  }

  ngOnInit() {
  }


}
