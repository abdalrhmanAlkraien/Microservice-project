import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { GatewayServiceSharedModule } from 'app/shared/shared.module';

import { DocsComponent } from './docs.component';

import { docsRoute } from './docs.route';

@NgModule({
  imports: [GatewayServiceSharedModule, RouterModule.forChild([docsRoute])],
  declarations: [DocsComponent],
})
export class DocsModule {}
