// Response
// tslint:disable-next-line:no-empty-interface
export interface Parameters {
}

export interface Text {
    text: string[];
}

export interface FulfillmentMessage {
    text: Text;
}

export interface Intent {
    name: string;
    displayName: string;
}

// tslint:disable-next-line:no-empty-interface
export interface DiagnosticInfo {
}

export interface QueryResult {
    queryText: string;
    parameters: Parameters;
    allRequiredParamsPresent: boolean;
    fulfillmentText: string;
    fulfillmentMessages: FulfillmentMessage[];
    intent: Intent;
    intentDetectionConfidence: number;
    diagnosticInfo: DiagnosticInfo;
    languageCode: string;
}

export interface GoogleResponse {
    responseId: string;
    queryResult: QueryResult;
}
