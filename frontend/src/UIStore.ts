import { observable, action } from "mobx";

export enum NotificationType {
  SUCCESS,
  ERROR
}

export enum ModalState {
  CREATED, SHOW, CANCELED, SAVED, REMOVED
}

export class Notification {
  id: string;
  type: NotificationType = NotificationType.SUCCESS;
  content: React.ReactElement | string;
}

export class ModalContent {
  actionEvent:(e)=>void;
  actionButton: string;
  title:string;
  content: React.ReactElement | string;
}

export interface UiStore{
  notifications: Notification[];
  modal: ModalState;
  modalContent:ModalContent;
  modelInDOM:boolean;
  updateModal:(modal:ModalState)=>void;
}

export const uiStore: UiStore = observable({
  notifications:[] as Notification[],
  modal:ModalState.REMOVED,
  modalContent:undefined,
  get modelInDOM() {
    return uiStore.modal === ModalState.CREATED ||
    uiStore.modal === ModalState.CANCELED ||
    uiStore.modal === ModalState.SAVED ||
    uiStore.modal === ModalState.SHOW;
  },
  updateModal:(modal:ModalState)=>{
    uiStore.modal = modal;
  }
},{updateModal:action});
