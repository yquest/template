import { observable, action, IObservable, IObservableArray } from "mobx";

export enum NotificationType {
  SUCCESS,
  ERROR
}

export enum ModalState {
  CREATED, SHOW, CANCELED, SAVED, REMOVED
}

export interface Notification {
  id: string;
  type: NotificationType;
  content: React.ReactElement | string;
}

export class ModalContent {
  actionEvent:(e:React.MouseEvent<any>)=>void;
  actionButton: string;
  title:string;
  content: React.ReactElement | string;
}

export interface UiStore{
  notifications: IObservableArray<Notification>;
  modal: ModalState;
  modalContent:ModalContent;
  modelInDOM:boolean;
  updateModalContent:(modalContent:ModalContent)=>void;
  updateModal:(modal:ModalState)=>void;
  carEditCalendarShow:boolean;
  toggleCarEditCalendar:()=>void;
  updateNotification(notification:Notification):void;
  removeNotification(index:number):void;
}

export const uiStore: UiStore = observable({
  notifications:[] as IObservableArray<Notification>,
  modal:ModalState.REMOVED,
  carEditCalendarShow:false,
  modalContent:undefined,
  get modelInDOM() {
    return uiStore.modal === ModalState.CREATED ||
    uiStore.modal === ModalState.CANCELED ||
    uiStore.modal === ModalState.SAVED ||
    uiStore.modal === ModalState.SHOW;
  },
  updateModal:(modal:ModalState)=>{
    uiStore.modal = modal;
  },
  toggleCarEditCalendar(){
    uiStore.carEditCalendarShow = !uiStore.carEditCalendarShow;
  },
  updateModalContent(modalContent:ModalContent){
    uiStore.modalContent = modalContent;
  },
  removeNotification(index:number){
    uiStore.notifications = uiStore.notifications.slice(index-1, index) as IObservableArray;
  },
  updateNotification(notification:Notification){
    uiStore.notifications.push(notification);
  }
},{updateModal:action, toggleCarEditCalendar:action, updateModalContent:action, updateNotification:action,removeNotification:action});
