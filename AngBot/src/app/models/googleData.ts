export interface Text {
  text: string;
  languageCode: string;
}

export interface QueryInput {
  text: Text;
}

export interface QueryParams {
  timeZone: string;
}

export interface GoogleData {
  queryInput: QueryInput;
  queryParams: QueryParams;
}
