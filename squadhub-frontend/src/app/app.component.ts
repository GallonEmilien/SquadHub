import { IonApp, IonRouterOutlet } from '@ionic/angular/standalone';

import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,

  imports: [IonApp, IonRouterOutlet],
})
export class AppComponent {
  constructor() {}
}
