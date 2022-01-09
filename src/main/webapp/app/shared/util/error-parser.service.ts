import { Injectable } from '@angular/core';
import { JhiAlertService } from 'ng-jhipster';

@Injectable()
export class ErrorParser {

    constructor(
        private alertService: JhiAlertService,
    ) { }

    parse(error){
        this.alertWithParams(error, {});
    }

    alertWithParams(error, params) {
        if (!error)
            return;

         /* 1. error format
        ----------------------------------------
            "message" : "error.internalServerError",
            "description" : "Insufficient stock for given Stock Details",
            "fieldErrors" : null,
            "errorMessages" : [ {
                "errorCode" : "10047",
                "source" : {
                "itemId" : 46,
                "availableQuantity" : 0.0,
                "itemName" : "Noroflex",
                "batchNo" : "Batch1",
                "stockId" : 56,
                "requestedQuantity" : 1.0,
                "storeId" : 113
                }
            } ]
        -----------------------------------------*/

        if (error.errorMessages && error.errorMessages.length > 0) {
            error.errorMessages.forEach(element => {
                const msgs = element;
                const errCode = (msgs.errorCode) ? 'error.code.' + msgs.errorCode : '';
                this.alertService.error(errCode ? errCode : error.message, msgs.source);
            });
        } else if (error.fieldErrors && error.fieldErrors.length > 0) {
            const msgs = error.fieldErrors[0];
            let errMsg = 'Following field is invalid : ';
            errMsg += (msgs.objectName ? msgs.objectName : '');
            errMsg += (msgs.field ? msgs.field : '');
            errMsg += (msgs.message ? msgs.message : '');
            this.alertService.error('global.messages.response-msg', {'msg': errMsg}, null);
        } else if (error.message === 'error.http.500') {
            // console.log(error.detail);
            const msg = error.detail? error.detail : (error.title? error.title: {'msg': 'Oops!!! Something went wrong'});
            this.alertService.error('global.messages.response-msg',  msg, null);
        } else if (error.message === 'error.concurrencyFailure') {
            this.alertService.error(error.message, null, null);
        } else if (error.key) {
            this.alertService.error(error.key, null, null);
        } else if (error.message === 'error.internalServerError') {
            this.alertService.error(error.message, error.params, null);
        } else if (error.message === 'error.http.403' || error.message === 'Not Found') {
            this.alertService.error('global.messages.response-msg', (error.detail? {'msg': error.detail}: (error.title ? error.title : {'msg': 'Oops!!! Something went wrong'}) ), null);
        } else if (error.message && error.message!=='error.internalServerError') {
            /* 3. error format
            ---------------------------------
                "message" : "10062",
                "params" : [ "itemId" : 46,
                            "availableQuantity" : 0.0,
                            "itemName" : "Noroflex ]
            ---------------------------------*/
            const errCode = (error.message) ? 'error.code.' + error.message : '';
            const paramsData = {};
            if (params) {
                Object.assign(paramsData, params);
            }
            if (error.params) {
                Object.assign(paramsData, error.params);
            }
            this.alertService.error(errCode ? errCode : error.message, paramsData, null);
        } else {
            /* 2. error format
                ---------------------------------"
                message" : "error.internalServerError",
                "description" : "Cannot get a NUMERIC value from a STRING cell",
                "fieldErrors" : null
                ----------------------------------*/
            this.alertService.error('global.messages.response-msg', (error.description? {'msg': error.description}: (error.params ? error.params : {'msg': 'Oops!!! Something went wrong'}) ), null);
        }
    }
}
