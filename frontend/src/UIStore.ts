import { observable, action, computed } from "mobx";

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
  actionButton: string;
  title:string;
  content: React.ReactElement | string;
}



export class UiStore {
  idxNotification: number = 0;
  @observable
  notifications: Notification[] = [];
  @observable
  modal: ModalState = ModalState.REMOVED;
  @observable
  modalAction:()=>any;
  @observable
  modalContent:ModalContent;

  createNotification() {
    let notification: Notification = new Notification();
    notification.id = `notif-${this.idxNotification++}`;
    return notification;
  }

  @action
  removeNotification(idx: string) {
    console.log(`removing ${idx}`);
    this.notifications = this.notifications.filter(el => {
      return el.id !== idx;
    });
  }

  private addNotification(notification: Notification): number {
    this.notifications.push(notification);
    return this.notifications.length - 1;
  }

  @action
  addNotificationTemp(notification: Notification, temp: number) {
    this.addNotification(notification);
    setTimeout(() => {
      this.removeNotification(notification.id);
    }, temp);
  }

  @action
  addNotificationPerm(notification: Notification) {
    this.addNotification(notification);
  }
  @action
  updateModal(modal: ModalState) {
    this.modal = modal;
  }
  @action
  updateModalContent(modalContent: ModalContent) {
    this.modalContent = modalContent;
  }
  @action
  updateModalAction(modalAction: ()=>any) {
    this.modalAction = modalAction;
  }
  @computed
  get modelInDOM(): boolean {
    return this.modal === ModalState.CREATED ||
    this.modal === ModalState.CANCELED ||
    this.modal === ModalState.SAVED ||
    this.modal === ModalState.SHOW;
  }
}

export const uiStore: UiStore = new UiStore();
