import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { HttpResponse } from '@angular/common/http';
import { ServiceMasterService } from './../../../administration/administration-services/service-master.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { SearchTermModify } from 'app/artha-helpers';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';

@Component({
  selector: 'jhi-applicable-services',
  templateUrl: './applicable-services.component.html'
})
export class ApplicableServicesComponent implements OnInit {
  serviceSearching: boolean;
  applicableServices: any = [];
  appServSearch: any;
  @Input() stayBenefitServicesListFromParent;

  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };

  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveScroll: PerfectScrollbarDirective;

  constructor(
    private serviceMasterService: ServiceMasterService,
    public activeModal: NgbActiveModal,
    private jhiAlertService: JhiAlertService,
    private searchTermModify: SearchTermModify
  ) {
    this.serviceSearching = false;
  }

  ngOnInit() {
    this.applicableServices = JSON.parse(JSON.stringify(this.stayBenefitServicesListFromParent));
  }

  serviceSearchTypeahead = (text$: Observable<string>) =>
    text$
      .debounceTime(500)
      .distinctUntilChanged()
      .do(term => {
        this.serviceSearching = term.length >= 1;
      })
      .switchMap(term =>
        term.length < 1
          ? Observable.of([])
          : this.serviceMasterService.search({ query: this.searchTermModify.modify(term) }).map((res: HttpResponse<any>) => {
              return res.body;
            })
      )
      .do(() => (this.serviceSearching = false));

  inputFormatServiceTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };
  formatServiceTypeheadData = (x: any) => {
    const valueX = x.name !== undefined ? x.name : '';
    return valueX;
  };

  cancel() {
    this.activeModal.dismiss('cancel');
  }

  done() {
    this.activeModal.close(this.applicableServices);
  }

  selectedApplicableServices(e) {
    if (this.applicableServices.find(ele => ele.serviceId === e.item.id)) {
      this.jhiAlertService.error('arthaApp.variable-payout.validation.componentExist');
      return;
    }

    const obj = {
      serviceCode: e.item.code,
      serviceId: e.item.id,
      serviceName: e.item.name
    };
    this.applicableServices.push(obj);
    this.appServSearch = { item: { name: '' } };
  }

  removeApplicableServices(index: number) {
    this.applicableServices.splice(index, 1);
  }
}
