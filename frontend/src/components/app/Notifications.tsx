import * as React from "react";
import { observer } from "mobx-react";
import { uiStore,Notification, NotificationType } from "../../UIStore";

function getClassNotificationType(type: NotificationType) {
  switch (type) {
    case NotificationType.SUCCESS:
      return "success";
    case NotificationType.ERROR:
      return "danger";
  }
}

@observer
export class Notifications extends React.Component<{}, {}> {
  render() {
    return (
      <div className="fixed-top container">
        <div className="float-right">
          {uiStore.notifications.map((notification: Notification) => (
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
