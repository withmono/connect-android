package mono.connect.widget;

public interface EventListener {
  void onClose();
  void onSuccess(ConnectedAccount account);
}
