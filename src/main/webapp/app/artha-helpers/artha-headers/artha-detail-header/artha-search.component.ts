import { Component, OnInit, ElementRef, Renderer2, AfterViewInit, EventEmitter, Output, Input, OnChanges, ViewChild } from '@angular/core';

import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subject } from 'rxjs/Subject';

import { JhiEventManager } from 'ng-jhipster';
import { fromEvent, of } from 'rxjs';
import { map, debounceTime, distinctUntilChanged, mergeMap } from 'rxjs/operators';
import { SearchTermModify } from 'app/artha-helpers/helper/search-term-modify.service';
@Component({
  selector: 'jhi-artha-search',
  templateUrl: './artha-search.component.html',
  providers: [SearchTermModify]
})
export class ArthaSearchComponent implements OnInit, OnChanges, AfterViewInit {
  addIcon = '../../../../content/images/header_images/icon_add_circle.svg';
  patientList: any[] = [];
  selectedLi: number;
  showUser = false;
  activeDescendant: string;
  patientListItems = [];
  patientSearching = false;
  userName: any;
  showAddBtn = false;
  modalRef: NgbModalRef;
  placeHolder = 'Search by Employee No. / Name';
  @Output() onUserSelection = new EventEmitter();
  @Output() addNewPatientEmitter = new EventEmitter();
  @Output() onPatientClear = new EventEmitter();
  @Output() onSearchUser = new EventEmitter();
  @Output() newDepartment = new EventEmitter();
  @Input() searchResultList;
  @Input() departmentSearch;
  @Input() disableSearch = false;
  @Input() inputHeight: string;
  @Input() showSearchIcon: boolean;
  @Input() displayMrn = false;
  @Input() clearable = false;
  @Input() validatePatient = false;
  @ViewChild('patientInput', { static: true }) inputDebounce: ElementRef;

  private searchSubject: Subject<string> = new Subject();
  public config: PerfectScrollbarConfigInterface = {
    minScrollbarLength: 50
  };

  patientClearSubscription: any;

  constructor(
    private elementRef: ElementRef,
    private renderer: Renderer2,
    private modalService: NgbModal,
    private eventManager: JhiEventManager,
    private searchTermModify: SearchTermModify
  ) {}

  ngOnInit() {
    // this.patientClearSubscription = this.eventManager.subscribe('clearPatient', () => {
    //     this.userName = '';
    //     if (this.elementRef.nativeElement.querySelector('#patientInput')) {
    //         this.elementRef.nativeElement.querySelector('#patientInput').value = '';
    //     }
    // });
  }

  ngOnChanges() {
    if (this.departmentSearch) {
      this.placeHolder = 'Search By Department';
    }
  }

  ngAfterViewInit() {
    // this.focus('#patientInput');
    const patientInput = this.elementRef.nativeElement.querySelector('#patientInput');
    this.renderer.listen(patientInput, 'keydown', this.onKeyDown);
  }
  onKeyUpEvent() {
    fromEvent(this.inputDebounce.nativeElement, 'keyup')
      .pipe(
        map((event: any) => {
          return event.target.value;
        }),
        debounceTime(500),
        distinctUntilChanged(),
        mergeMap(search => this.getValues(search))
      )
      .subscribe(() => {});
  }
  getValues(search): any {
    if (!search || (search && search.length < 1)) {
      this.onSearchUser.emit(null);
      this.userName = '';
      return of([]);
    }
    this.showUser = true;
    const searchTerm = this.searchTermModify.modify(search);
    this.onSearchUser.emit(searchTerm);
    return search;
    /**
     * 
        if (!text || (text && text.length < 1)) {
          this.onSearchUser.emit(null);
          this.userName = '';
          return;
        }
        this.showUser = true;
        const searchTerm = this.searchTermModify.modify(text);
        this.onSearchUser.emit(searchTerm);
      
     */
  }
  onKeyUp(searchTextValue: string) {
    // this.searchSubject.next(searchTextValue);
    this.searchPatient(searchTextValue);
  }

  addDepartment() {
    this.newDepartment.emit();
  }

  // focus(selector) {
  //     setTimeout(() => {
  //         this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector(selector), 'focus', []);
  //     }, 0);
  // }

  searchPatient(term) {
    if (!term || (term && term.length < 1)) {
      this.onSearchUser.emit(null);
      this.userName = '';
      return;
    }
    this.showUser = true;

    this.onSearchUser.emit(term);
  }

  setPatientListStyle() {
    setTimeout(() => {
      this.activeDescendant = 'patient-list-item-0';
      this.selectedLi = 0;
      this.patientListItems = this.elementRef.nativeElement.querySelectorAll('div.patient-list-item');
    }, 0);
  }

  setMouseOverStyle(id: any) {
    const prevItem = document.querySelector('div.patient-list-item.selected');
    if (prevItem) {
      prevItem.className = 'patient-list-item';
    }
    const li = this.elementRef.nativeElement.querySelector('#patient-list-item-' + id);
    if (li) {
      // this.renderer.setElementClass(li, 'selected', true);
    }
  }

  onKeyDown = $event => {
    const key = $event.key;
    switch (key) {
      case 'ArrowDown':
        this.next();
        $event.preventDefault();
        break;
      case 'ArrowUp':
        this.prev();
        $event.preventDefault();
        break;
      case 'Enter':
        this.onEnterKey();
        $event.preventDefault();
        break;
      case 'Tab':
        this.onTabKey();
        break;
      default:
      // do nothing
    }
  };

  onTabKey() {
    // this.eventManager.broadcast({ name: 'patientSearchTabPress' });
  }

  onRemovingSelected() {
    this.userName = '';
    this.onSearchUser.emit(null);
    // this.eventManager.broadcast({ name: 'patientSearchOnRemovalPress' });
  }

  prev() {
    const listCount = this.patientListItems.length;
    if (listCount === 0) {
      return;
    }
    if (this.selectedLi === 0) {
      return;
    }
    this.selectedLi -= 1;
    this.setScroll();
  }

  next() {
    const listCount = this.patientListItems.length;
    if (listCount === 0) {
      return;
    }
    if (this.selectedLi === listCount - 1) {
      return;
    }
    this.selectedLi += 1;
    this.setScroll();
  }

  setScroll() {
    this.activeDescendant = 'patient-list-item-' + this.selectedLi;
    const li = this.patientListItems[this.selectedLi];
    const patientDiv = this.elementRef.nativeElement.querySelector('div.patient-list-box');
    const elHeight = li.clientHeight;
    const scrollTop = patientDiv.scrollTop;
    const viewport = scrollTop + patientDiv.clientHeight;
    const elOffset = elHeight * this.selectedLi;
    if (elOffset < scrollTop || elOffset + elHeight > viewport) {
      patientDiv.scrollTop = elOffset;
    }
  }

  onPatientListClickOutside() {
    this.showUser = false;
  }

  onEnterKey() {
    if (this.selectedLi || this.selectedLi === 0) {
      const patient = this.patientList[this.selectedLi];
      this.selectPatient(patient);
    }
  }

  selectPatient(patient) {
    this.userName = '';

    this.onPatientSelected(patient);
  }

  onPatientSelected(user: any) {
    this.userName = user.name;
    this.onUserSelection.emit(user);
    this.showUser = false;
  }

  addNew() {
    this.addNewPatientEmitter.emit();
  }
}
