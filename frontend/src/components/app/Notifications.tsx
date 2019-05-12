import * as React from "react";
import { observer } from "mobx-react";
import { observable, action } from "mobx";

export enum NotificationType {
  SUCCESS,
  ERROR
}

export class Notification {
  id: string;
  type: NotificationType = NotificationType.SUCCESS;
  content: React.ReactElement|string;
}

export class NotificationStore {
  idx: number = 0;
  @observable
  notifications: Notification[] = [];

  createNotification() {
    let notification: Notification = new Notification();
    notification.id = `notif-${this.idx++}`;
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
}

function getClassNotificationType(type: NotificationType) {
  switch (type) {
    case NotificationType.SUCCESS:
      return "success";
    case NotificationType.ERROR:
      return "danger";
  }
}

export const notificationStore: NotificationStore = new NotificationStore();

@observer
export class Notifications extends React.Component<{}, {}> {
  render() {
    return (
      <div className="fixed-top container">
        <div className="float-right">
          {notificationStore.notifications.map((notification: Notification) => (
            <div
              key={notification.id}
              className={` alert alert-${getClassNotificationType(
                notification.type
              )}`}
              role="alert"
            >
              {notification.content}
            </div>
          ))}
        </div>
      </div>
    );
  }
}
