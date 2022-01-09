import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
// import * as momentImported from 'moment';
import { DaterangepickerConfig } from 'ng2-daterangepicker';
import { SessionStorage, SessionStorageService } from 'ngx-webstorage';
import { Observable, of } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, switchMap, tap } from 'rxjs/operators';
import { Preferences } from 'app/entities/artha-models/preferences.model';
import { AdvanceSearchService } from '../services/advance-search.service';
import { UserPersonal, TypeHeadModel, DateModel, SelectModel, InputModel } from './advance-search.model';
import { PreferenceService } from '../services/preference.service';
// const moment = momentImported;
@Component({
  selector: 'jhi-advance-search',
  templateUrl: './advance-search.component.html'
})
export class AdvanceSearchComponent implements OnInit {
  @Input() pageName;
  @Input() feature;
  @Input() subFeature;
  @Input() moduleName;
  @Input() inputformElement;
  @Input() selectformElement;
  @Input() dateformElement;
  @Input() typeHeadformElement;
  @Input() userLogin;
  @Input() unitElement;
  @Input() hscStoreElement;
  @Input() serviceCenterElement;
  @Output() dismiss: EventEmitter<string> = new EventEmitter<string>();
  @Output() saveAndClose: EventEmitter<any> = new EventEmitter<any>();
  // @Input() userMappedHscs;
  organization: any;

  @SessionStorage('advSearchDetail')
  public advSearchDetail;

  lastQuery: any;
  queryStr: any;

  newInputElement = [];
  newTypeheadEle = [];
  newSelectElement = [];
  newDateElement = [];
  serviceList = [];
  searchStr: string;
  searchQueryList: any;
  errMsg = '';
  sucessMsg = '';
  invalidSearchName = true;
  newHscStore: TypeHeadModel;
  newServiceCenter: any;
  searchQuery = '';
  typeheadIndex: number;
  options: any;
  unit: any;
  hscStore: any;
  activeSearch: number;
  hscSearchStr: any;
  unitSerachStr: any;
  unitSearching: boolean;
  statusOptions: any;
  storeSearchData: any;
  unitStoreSearching: boolean;
  hscStoreSearching: boolean;
  dateFormat = 'dd/MM/yyyy';
  preferences: Preferences;
  datePickerMaxDate = new Date();

  closeResult: string;
  private eventLog = '';
  public queryName;
  public storeSearchStr: any;
  public settingType = 'ADVANCE_SEARCH';

  buildQueryAsRequestParams = false;

  queryStringAsRequestParams = {};
  constructor(
    public daterangepickerOptions: DaterangepickerConfig,
    public advanceSearchService: AdvanceSearchService,
    private sessionStorage: SessionStorageService,
    private preferencesService: PreferenceService
  ) {
    this.statusOptions = [];
    this.sessionStorage.store('advSearchDetail', null);
    // this.preferencesService.currentUser().subscribe((pref) => {
    //   this.preferences = pref;
    // });
    this.preferences = this.preferencesService.currentUser();
  }

  ngOnInit() {
    this.options = {
      maxDate: this.datePickerMaxDate,
      autoApply: true,
      locale: {
        format: 'DD/MM/YYYY'
      }
    };

    if (!this.subFeature) {
      this.subFeature = this.feature;
    }

    this.lastQuery = {};

    this.advSearchDetail = this.sessionStorage.retrieve('advSearchDetail');
    if (this.advSearchDetail && this.feature === this.advSearchDetail.feature && this.subFeature === this.advSearchDetail.subFeature) {
      this.statusOptions = this.advSearchDetail.statusOptions ? this.advSearchDetail.statusOptions : [];
      this.statusOptions.sort((m1, m2): number => {
        if (m1.name < m2.name) return -1;
        if (m1.name > m2.name) return 1;
        return 0;
      });
      this.storeSearchData = this.advSearchDetail.storeOptions;
      this.newInputElement = this.advSearchDetail.newInputElement;
      this.newTypeheadEle = this.advSearchDetail.newTypeheadEle;
      this.newHscStore = this.advSearchDetail.newHscStore;
      this.newServiceCenter = this.advSearchDetail.newServiceCenter;
      this.newSelectElement = this.advSearchDetail.newSelectElement;
      // this.newSelectElement.length?(this.newSelectElement[0].optionData=this.statusOptions):[];
      if (this.newSelectElement.length > 0) {
        for (let i = 0; i < this.newSelectElement.length; i++) {
          for (let j = 0; j < this.statusOptions.length; j++) {
            if (this.newSelectElement[i].fieldName === this.statusOptions[j].name) {
              this.newSelectElement[i].optionData = this.statusOptions[j].options;
            }
          }
        }
      }

      this.newTypeheadEle.length ? (this.newTypeheadEle[0].searchData = this.storeSearchData) : [];
      this.newDateElement = this.advSearchDetail.newDateElement;
      this.unit = this.advSearchDetail.unit;
      this.activeSearch = this.advSearchDetail.activeSearch ? this.advSearchDetail.activeSearch : null;
    } else {
      if (this.inputformElement && this.inputformElement.length > 0) {
        this.inputformElement.map((datObj: any) => {
          const data = new InputModel();
          data.fieldName = datObj.fieldName;
          data.queryName = datObj.queryName;
          this.newInputElement.push(data);
        });
      }

      if (this.typeHeadformElement && this.typeHeadformElement.length > 0) {
        this.typeHeadformElement.map((datObj: any) => {
          const data = new TypeHeadModel();
          data.fieldName = datObj.fieldName;
          data.queryName = datObj.queryName;
          if (datObj.searchData) {
            data.searchData = datObj.searchData;
          }
          if (datObj.url) {
            data.url = datObj.url;
          }
          if (datObj.displayField) {
            data.displayField = datObj.displayField;
          }
          if (datObj.typeHeadSearchQuery) {
            data.typeHeadSearchQuery = datObj.typeHeadSearchQuery;
          }
          if (datObj.typeHeadSearchResultFilter) {
            data.typeHeadSearchResultFilter = datObj.typeHeadSearchResultFilter;
          }
          if (datObj.sortBasedOn) {
            data.sortBasedOn = datObj.sortBasedOn;
          }
          data.inputFormat = datObj.inputFormat;
          data.resultFormatter = datObj.resultFormatter;
          this.storeSearchData = datObj.searchData ? datObj.searchData : [];
          this.newTypeheadEle.push(data);
        });
      }

      if (this.selectformElement && this.selectformElement.length > 0) {
        this.selectformElement.map((datObj: any) => {
          const data = new SelectModel();
          data.fieldName = datObj.fieldName;
          data.queryName = datObj.queryName;
          data.optionData = datObj.optionData;
          data.translation = datObj.valueTranslation === undefined ? true : datObj.valueTranslation;
          this.statusOptions.push({ name: datObj.fieldName, options: datObj.optionData });
          this.statusOptions.sort((m1, m2): number => {
            if (m1.options.name < m2.options.name) return -1;
            if (m1.options.name > m2.options.name) return 1;
            return 0;
          });
          this.newSelectElement.push(data);
        });
      }

      if (this.dateformElement && this.dateformElement.length > 0) {
        this.dateformElement.map((datObj: any) => {
          const data = new DateModel();

          data.fieldName = datObj.fieldName;
          data.queryName = datObj.queryName;
          if (datObj.includeTimeInQuery) {
            data.includeTimeInQuery = datObj.includeTimeInQuery;
          }
          this.newDateElement.push(data);
        });
      }

      if (this.hscStoreElement && this.hscStoreElement.length > 0) {
        this.newHscStore = new TypeHeadModel();
        this.newHscStore.fieldName = this.hscStoreElement[0].fieldName;
        this.newHscStore.queryName = this.hscStoreElement[0].queryName;
        this.newHscStore.labelName = this.hscStoreElement[0].labelName;
        this.newHscStore.tempData = [];
      }

      if (this.serviceCenterElement && this.serviceCenterElement.length > 0) {
        this.newServiceCenter = {
          isDisplay: false
        };
        this.newServiceCenter.fieldName = this.serviceCenterElement[0].fieldName;
        this.newServiceCenter.queryName = this.serviceCenterElement[0].queryName;
        this.newServiceCenter.labelName = this.serviceCenterElement[0].labelName;
        this.newServiceCenter.tempData = [];
      }

      if (this.unitElement && this.unitElement.length > 0) {
        this.unit = new TypeHeadModel();
        this.unit.fieldName = this.unitElement[0].fieldName;
        this.unit.queryName = this.unitElement[0].queryName;
        this.unit.tempData = [];
      }
    }
    this.getSaveSearchQuery();

    // this.loadDefaultServiceLCenterList();
  }

  getCurrentIndex(index) {
    this.typeheadIndex = index;
  }

  searchTypeAhead = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      tap((term: string) => (this.storeSearchStr = term.length >= 1)),
      switchMap((term: string) => {
        if (!this.newTypeheadEle[this.typeheadIndex].url) {
          return of(
            this.newTypeheadEle[this.typeheadIndex].searchData.filter(f => {
              return f.code.toLowerCase().indexOf(term.toLowerCase()) !== -1 || f.name.toLowerCase().indexOf(term.toLowerCase()) !== -1;
            })
          );
        } else {
          return term.length < 1 ? of([]) : this.typeAheadSearch(term);
        }
      }),
      tap(() => (this.storeSearchStr = false))
    );

  typeAheadSearch = (term: string) => {
    // const searchQuery: string = this.newTypeheadEle[this.typeheadIndex].typeHeadSearchQuery;
    // let q = searchQuery.replace(new RegExp('searchValue', 'gi'), this.convertToSearchStr(term));
    // sort: this.newTypeheadEle[this.typeheadIndex].sortBasedOn
    if (this.newTypeheadEle[this.typeheadIndex].url === 'api/_search/departments') {
      term = 'active: true AND organization.id:' + this.preferences.organization.id + ' **' + term + '**';
    }
    return this.advanceSearchService
      .typeHeadSearch(
        this.newTypeheadEle[this.typeheadIndex].url,
        { query: term.trim() ? term : '*' },
        this.newTypeheadEle[this.typeheadIndex].typeHeadSearchResultFilter
      )
      .pipe(map(res => res));
  };

  // public mainInput = {
  //   start: moment().subtract(12, 'month'),
  //   end: moment().subtract(6, 'month')
  // };

  public calendarEventsHandler(e: any) {
    this.eventLog += '\nEvent Fired: ' + e.event.type;
  }

  public selectedDate(value: any, dateIndex: any) {
    this.newDateElement[dateIndex].isDateModified = true;
    this.newDateElement[dateIndex].startDate = value.start.format('YYYY-MM-DD');
    this.newDateElement[dateIndex].endDate = value.end.format('YYYY-MM-DD');
  }

  removeInput(inputIndex) {
    this.newInputElement[inputIndex].name = '';
    this.newInputElement[inputIndex].isDisplay = false;
  }

  removeTypeHead(tyIndex) {
    this.newTypeheadEle[tyIndex].tempData = [];
    this.newTypeheadEle[tyIndex].isDisplay = false;
  }

  removeSelect(selectIndex) {
    this.newSelectElement[selectIndex].tempData = [];
    this.newSelectElement[selectIndex].isDisplay = false;
    // Reload bedDefinition Service center
    if (this.newSelectElement[selectIndex].optionData[0] && this.newSelectElement[selectIndex].optionData[0].unitId) {
      this.newServiceCenter.tempData = [];
    }
  }

  removeDate(dateIndex) {
    this.newDateElement[dateIndex].startDate = new Date().toISOString().split('T')[0];
    this.newDateElement[dateIndex].endDate = new Date().toISOString().split('T')[0];
    this.newDateElement[dateIndex].isDateModified = false;
    this.newDateElement[dateIndex].isDisplay = false;
  }

  removeUnit() {
    this.unit.tempData = [];
    this.unit.codeData = [];
    this.unit.isDisplay = false;
    this.checkUnitonCode();
  }

  removeHSC() {
    this.newHscStore.tempData = [];
    this.newHscStore.isDisplay = false;
  }

  removeServiceCenter() {
    this.newServiceCenter.tempData = [];
    this.newServiceCenter.isDisplay = false;
  }

  continue() {
    this.activeSearch = null;
    this.valiDateQuerydata();
    this.sessionStorage.store('advSearchDetail', this.lastQuery);
    this.saveAndClose.emit(this.buildQueryAsRequestParams ? this.queryStringAsRequestParams : this.searchQuery);
  }

  fetchSearchQuery(obj) {
    this.activeSearch = obj.id;
    this.setQueryObj(obj.settingValue.value);
    this.valiDateQuerydata();
    this.sessionStorage.store('advSearchDetail', this.lastQuery);
    this.saveAndClose.emit(this.searchQuery);
  }

  onchangeStatus(statusData, indexData) {
    if (statusData.target.value) {
      if (this.newSelectElement[indexData].tempData.includes(statusData.target.value)) {
        // console.log('value already exist');
      } else {
        this.newSelectElement[indexData].tempData.push(statusData.target.value);
        // for bedDefinition unit and service mapping
        if (this.newSelectElement[indexData].optionData[0].unitId) {
          let unitIdList: any = [];
          this.newSelectElement[indexData].optionData.forEach(outerElement => {
            this.newSelectElement[indexData].tempData.forEach(innerElement => {
              if (outerElement.name === innerElement) {
                unitIdList.push(outerElement.unitId);
              }
            });
          });
          if (unitIdList.length > 0) {
            unitIdList = '(' + unitIdList.join(' OR ') + ')';
          }
        }
      }
    }
    statusData.target.value = '';
  }

  onChangeBedService(data: any) {
    data.preventDefault();
    if (!this.newServiceCenter.tempData.includes(data.target.value)) {
      this.newServiceCenter.tempData.push(data.target.value);
    }
    data.target.value = '';
  }

  selectTypehead(data: any, inputStore, tyIndex) {
    data.preventDefault();
    this.newTypeheadEle[tyIndex].tempData.push(data.item[this.newTypeheadEle[tyIndex].displayField]);
    inputStore.value = '';
  }

  selectUnit(data: any, inputStore) {
    data.preventDefault();
    this.unit.tempData.push(data.item.name);
    this.unit.codeData.push(data.item.code);
    inputStore.value = '';
  }

  selectHscStore(data: any, inputStore) {
    data.preventDefault();
    this.newHscStore.tempData.push(data.item.name);
    this.newHscStore.codeData.push(data.item.code);
    inputStore.value = '';
  }

  displayTypehead(index) {
    this.newTypeheadEle[index].isDisplay = true;
  }

  displayDateElement(index) {
    this.newDateElement[index].isDateModified = true;
    this.newDateElement[index].isDisplay = true;
  }

  displaySelectElement(index) {
    this.newSelectElement[index].isDisplay = true;
    if (this.newSelectElement[index]?.optionData[0]?.unitId) {
      this.loadDefaultUnit();
    }
  }

  displayUnit() {
    this.unit.isDisplay = true;
  }

  displayStore() {
    this.newHscStore.isDisplay = true;
  }

  displayServiceCenter() {
    this.newServiceCenter.isDisplay = true;
  }

  removeTypeHeadTempData(thIndex, indexdata) {
    this.newTypeheadEle[thIndex].tempData.splice(indexdata, 1);
  }

  removeUnitData(indexdata) {
    this.unit.tempData.splice(indexdata, 1);
    this.unit.codeData.splice(indexdata, 1);
    this.checkUnitonCode();
  }

  removehscStoreData(indexdata) {
    this.newHscStore.tempData.splice(indexdata, 1);
  }

  removeServiceCenterData(indexData) {
    this.newServiceCenter.tempData.splice(indexData, 1);
  }

  removeSelectChip(chipIndex, seelctIndex) {
    this.newSelectElement[chipIndex].tempData.splice(seelctIndex, 1);
    // for bedDefinition unit and service mapping
    if (this.newSelectElement[chipIndex].optionData[0] && this.newSelectElement[chipIndex].optionData[0].unitId) {
      let unitIdList: any = [];
      this.newSelectElement[chipIndex].optionData.forEach(outerElement => {
        this.newSelectElement[chipIndex].tempData.forEach(innerElement => {
          if (outerElement.name === innerElement) {
            unitIdList.push(outerElement.unitId);
          }
        });
      });
      this.newServiceCenter.tempData = [];
      if (unitIdList.length > 0) {
        unitIdList = '(' + unitIdList.join(' OR ') + ')';
      }
    }
  }

  valiDateQuerydata() {
    this.searchQuery = '';
    if (this.newInputElement.length > 0) {
      this.newInputElement.map(resObj => {
        if (resObj.name && resObj.queryName) {
          this.searchQuery = this.searchQuery + resObj.queryName + ':' + this.convertToSearchStr(resObj.name) + ' ';
          if (this.buildQueryAsRequestParams) {
            this.queryStringAsRequestParams[resObj.queryName] = this.convertToSearchStr(resObj.name);
          }
        }
      });
    }

    if (this.newTypeheadEle.length > 0) {
      this.newTypeheadEle.map(resObj => {
        if (resObj.tempData && resObj.queryName && resObj.tempData.length > 0) {
          this.searchQuery = this.searchQuery + resObj.queryName + ':' + this.convertArrayToSearchStr(resObj.tempData) + ' ';
          if (this.buildQueryAsRequestParams) {
            this.queryStringAsRequestParams[resObj.queryName] = this.convertArrayToSearchStr(resObj.tempData);
          }
        }
      });
    }

    if (this.unit) {
      if (this.unit.tempData && this.unit.queryName && this.unit.tempData.length > 0) {
        this.searchQuery = this.searchQuery + this.unit.queryName + ':' + this.convertArrayToSearchStr(this.unit.tempData) + ' ';
        if (this.buildQueryAsRequestParams) {
          this.queryStringAsRequestParams[this.unit.queryName] = this.convertArrayToSearchStr(this.unit.tempData);
        }
      }
    }

    if (this.newHscStore) {
      if (this.newHscStore.tempData && this.newHscStore.queryName && this.newHscStore.tempData.length > 0) {
        this.searchQuery =
          this.searchQuery + this.newHscStore.queryName + ':' + this.convertArrayToSearchStr(this.newHscStore.tempData) + ' ';
        if (this.buildQueryAsRequestParams) {
          this.queryStringAsRequestParams[this.newHscStore.queryName] = this.convertArrayToSearchStr(this.newHscStore.tempData);
        }
      }
    }

    if (this.newServiceCenter) {
      if (this.newServiceCenter.tempData && this.newServiceCenter.queryName && this.newServiceCenter.tempData.length > 0) {
        this.searchQuery =
          this.searchQuery + this.newServiceCenter.queryName + ':' + this.convertArrayToSearchStr(this.newServiceCenter.tempData) + ' ';
        if (this.buildQueryAsRequestParams) {
          this.queryStringAsRequestParams[this.newServiceCenter.queryName] = this.convertArrayToSearchStr(this.newServiceCenter.tempData);
        }
      }
    }

    if (this.newSelectElement.length > 0) {
      this.newSelectElement.map(resObj => {
        if (resObj.tempData && resObj.queryName && resObj.tempData.length > 0) {
          this.searchQuery = this.searchQuery + resObj.queryName + ':' + this.convertArrayToSearchStr(resObj.tempData) + ' ';
          if (this.buildQueryAsRequestParams) {
            this.queryStringAsRequestParams[resObj.queryName] = this.convertArrayToSearchStr(resObj.tempData);
          }
        }
      });
    }

    if (this.newDateElement.length > 0) {
      this.newDateElement.map(resObj => {
        if (resObj.isDateModified && resObj.startDate && resObj.queryName) {
          const newDate = resObj.startDate;
          const startDate = newDate;
          const endtDate = resObj.endDate;
          if (resObj.includeTimeInQuery === false) {
            this.searchQuery = this.searchQuery + resObj.queryName + ':' + '[' + startDate + ' TO ' + endtDate + '] ';
            if (this.buildQueryAsRequestParams) {
              this.queryStringAsRequestParams[resObj.queryName] = '[' + startDate + ' TO ' + endtDate + '] ';
            }
          } else {
            this.searchQuery =
              this.searchQuery + resObj.queryName + ':' + '[' + startDate + 'T00:00:00' + ' TO ' + endtDate + 'T23:59:59' + '] ';
            if (this.buildQueryAsRequestParams) {
              this.queryStringAsRequestParams[resObj.queryName] = '[' + startDate + 'T00:00:00' + ' TO ' + endtDate + 'T23:59:59' + '] ';
            }
          }
        }
      });
    }
    this.setLastQuery();
  }

  setLastQuery() {
    if (this.newSelectElement.length) {
      // delete (this.newSelectElement[0]['optionData']);
    }
    if (this.newTypeheadEle.length) {
      delete this.newTypeheadEle[0].searchData;
    }

    this.lastQuery = {
      newInputElement: this.newInputElement,
      newTypeheadEle: this.newTypeheadEle,
      newSelectElement: this.newSelectElement,
      newDateElement: this.newDateElement,
      unit: this.unit,
      newHscStore: this.newHscStore,
      newServiceCenter: this.newServiceCenter,
      feature: this.feature,
      subFeature: this.subFeature,
      storeOptions: this.storeSearchData,
      statusOptions: this.statusOptions,
      activeSearch: this.activeSearch
    };
  }

  setQueryObj(obj) {
    this.newInputElement = obj.newInputElement;
    this.newTypeheadEle = obj.newTypeheadEle;
    this.newSelectElement = obj.newSelectElement;
    this.newDateElement = obj.newDateElement;
    this.unit = obj.unit;
    this.newHscStore = obj.newHscStore;
    this.newServiceCenter = obj.newServiceCenter;
    this.feature = obj.feature;
    this.subFeature = obj.subFeature;
  }

  checkQueryName($event) {
    const queryName = $event.target.value.replace(/  +/g, ' ');
    if (queryName.length > 2) {
      this.errMsg = '';
      this.invalidSearchName = false;
    } else {
      this.errMsg = 'Minimum three character';
      this.invalidSearchName = true;
    }

    if (queryName.length > 20) {
      this.errMsg = 'More than 20 character not allow';
      this.invalidSearchName = true;
      this.sucessMsg = '';
    }
  }

  saveSearch() {
    this.valiDateQuerydata();
    if (this.searchQuery === '') {
      this.errMsg = 'Empty search query';
      this.sucessMsg = '';
      return false;
    }

    if (this.queryName && this.userLogin) {
      const data = new UserPersonal();
      data.feature = this.feature;
      data.subFeature = this.subFeature;
      data.module = this.moduleName;
      data.settingType = this.settingType;
      data.settingValue = { name: this.queryName, value: this.lastQuery };
      data.userName = this.userLogin;
      this.advanceSearchService.create(data).subscribe(
        () => {
          this.getSaveSearchQuery();
          this.queryName = '';
          this.errMsg = '';
          this.sucessMsg = 'Sucessfully Saved !';
        },
        () => {
          this.errMsg = 'Unable to Save !';
          this.sucessMsg = '';
        }
      );
    } else {
      this.errMsg = 'empty search query !';
      this.sucessMsg = '';
    }
  }

  getSaveSearchQuery() {
    const query = {
      query:
        'userName:' +
        this.userLogin +
        ' module:' +
        this.moduleName +
        ' feature:' +
        this.feature +
        ' subFeature:' +
        this.subFeature +
        ' settingType:' +
        this.settingType,
      sort: ['createdDate,desc'],
      size: 10
    };
    this.advanceSearchService.search(query).subscribe(res => {
      this.searchQueryList = res;
    });
  }

  removeSearchQuery(id) {
    this.advanceSearchService.delete(id).subscribe(() => {
      this.sucessMsg = 'Sucessfully Removed !';
      this.getSaveSearchQuery();
    });
  }

  checkUnitonCode() {
    // console.log(this.queryStr);
    this.queryStr = '';
    if (this.unit && this.unit.codeData.length > 0) {
      this.queryStr = this.unit.codeData.join(' OR ');
    }
  }

  loadDefaultServiceLCenterList() {
    if (this.advSearchDetail && this.advSearchDetail.newSelectElement) {
      this.newSelectElement.forEach(element => {
        if (element.optionData[0] && element.optionData[0].unitId) {
          let unitIdList: any = [];
          element.optionData.forEach(outerElement => {
            element.tempData.forEach(innerElement => {
              if (outerElement.name === innerElement) {
                unitIdList.push(outerElement.unitId);
              }
            });
          });
          if (unitIdList.length > 0) {
            unitIdList = '(' + unitIdList.join(' OR ') + ')';
          }
        }
      });
    }
  }
  // TODO : Later

  // searchHscStores = (text$: Observable<string>) =>
  //   text$.pipe(
  //     debounceTime(500),
  //     distinctUntilChanged(),
  //     tap((term: any) => {
  //       this.hscSearchStr = (term.length >= 1);
  //     }),
  //     switchMap((term: any) => term.length < 1 ? of([])
  //       : this.healthcareServiceCenterService.search(
  //         { 'query': (this.queryStr) ? 'active:true partOf.code:(' + this.queryStr + ') ' : '' + term + '*' }).pipe(map((res) => {
  //           return res.body;
  //         })),
  //       tap(() => this.hscStoreSearching = false)));

  // formatHscData = (x: HealthcareServiceCenter) => x.code + ' | ' + x.name;
  // inputFormatHscData = (x: HealthcareServiceCenter) => x.name;

  // DON'T REMOVE THE FOLLOWING CODE
  //
  //
  // searchHscStores = (text$: Observable<string>) =>
  //   text$
  //     .debounceTime(500)
  //     .distinctUntilChanged()
  //     .do((term) => {
  //       this.hscSearchStr = (term.length >= 1);
  //     })
  //     .switchMap(term => term.length > 1? this.queryStr ?
  //       this.healthcareServiceCenterService.search({ 'query':'active:true partOf.code:(' + this.queryStr +') ' + term + '*' }).map((res) => {
  //         return res.json();
  //       }):this.userMappedHscs ? Observable.of(this.userMappedHscs.filter(hsc => {
  //         return hsc.code.toLowerCase().indexOf(term.toLowerCase()) != -1 || hsc.name.toLowerCase().indexOf(term.toLowerCase()) != -1;
  //       })):[]:[])
  //     .do(() => this.hscStoreSearching = false);

  // formatHscData = (x: any) => x.code + ' | ' + x.name;
  // inputFormatHscData = (x: any) => x.name;
  //
  //
  // DON'T REMOVE THE ABOVE CODE

  // Unit serach globally
  // searchUnits = (text$: Observable<string>) =>
  //   text$.pipe(
  //     debounceTime(500),
  //     distinctUntilChanged(),
  //     tap((term: any) => {
  //       this.unitSerachStr = (term.length >= 1)
  //     }),
  //     switchMap((term: any) => term.length < 1 ? of([])
  //       : this.organizationService.search({ 'query': 'active:true type.code.raw:prov ' + term + '*' }).pipe(map((res) => {
  //         return res.body;
  //       })),
  //       tap(() => this.unitSearching = false)));

  // formatUnitsData = (x: Organization) => x.code + ' | ' + x.name;
  // inputFormatUnitsData = (x: Organization) => x.name;

  convertToSearchStr(data) {
    // let rawData = data.trim();
    // rawData = rawData.replace(/\+/g, ' ');
    // rawData = rawData.replace(/ /g, '');
    // rawData = rawData.replace(/  +/g, ' ');
    // rawData = rawData.replace(/"/g, '');
    // // eslint-disable-next-line no-useless-escape
    // rawData = rawData.replace(/\:/g, '\\:');
    // rawData = rawData.replace(/\//g, '\\/');
    // rawData = rawData.replace(/\[/g, '\\[');
    // rawData = rawData.replace(/\]/g, '\\]');
    // rawData = rawData.replace(/\(/g, '\\(');
    // rawData = rawData.replace(/\)/g, '\\)');
    let convertStr = '(';
    if (data.indexOf(',') !== -1) {
      const strData = data.split(',');
      strData.map((res, index) => {
        let str = '(';
        const strData1 = res.split(' ');
        strData1.map((res1, indexData) => {
          if (indexData + 1 < strData1.length) str += '*' + res1 + '*' + ' ';
          else str += '*' + res1 + '*';
        });
        str += ')';
        if (index + 1 < strData.length) convertStr += str + ' OR ';
        else convertStr += str;
      });
      convertStr += ')';
      return convertStr;
    } else {
      const splitStr = data.split(' ');
      let convertStrData = '';
      splitStr.map(function(obj) {
        convertStrData += '*' + obj + '*' + ' ';
      });
      return convertStrData;
      // return '*' + data + '*';
    }
  }

  loadDefaultUnit() {
    this.newSelectElement.forEach(ele1 => {
      if (ele1.optionData[0].unitId) {
        ele1.optionData.forEach(ele2 => {
          if (ele2.unitId === this.preferences.hospital.id) {
            ele1.tempData.push(ele2.value);
          }
        });
      }
    });
  }

  convertArrayToSearchStr(data) {
    const docData = data;
    let str = '(';
    for (let i = 0; i < docData.length; i++) {
      if (i !== docData.length - 1) {
        // eslint-disable-next-line no-useless-escape
        str = str + '("' + docData[i] + '") OR ';
      } else {
        // eslint-disable-next-line no-useless-escape
        str = str + '("' + docData[i] + '")';
      }
    }
    str = str + ')';
    str = str.replace(/\[/g, '\\[');
    str = str.replace(/\]/g, '\\]');
    return str;
  }

  cancel() {
    this.dismiss.emit('Cancel');
  }
}
