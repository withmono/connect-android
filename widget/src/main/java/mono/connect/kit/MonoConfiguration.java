package mono.connect.kit;

import android.content.Context;

public class MonoConfiguration {

    // required
    public String publicKey;
    public Context context;
    public ConnectSuccessCallback onSuccess;

    // optionals
    public String reference;
    public String scope;
    public String accountId;
    public ConnectCloseCallback onClose;
    public ConnectEventCallback onEvent;
    public MonoInstitution selectedInstitution;
    public MonoCustomer customer;

    private MonoConfiguration(Builder builder) {
        this.context = builder.context;
        this.publicKey = builder.publicKey;
        this.reference = builder.reference;
        this.scope = builder.scope;
        this.accountId = builder.accountId;
        this.onSuccess = builder.onSuccess;
        this.onClose = builder.onClose;
        this.onEvent = builder.onEvent;
        this.selectedInstitution = builder.selectedInstitution;
        this.customer = builder.customer;
    }

    public static class Builder {
        // required
        private final Context context;
        private final String publicKey;
        private final ConnectSuccessCallback onSuccess;

        // optionals
        private String reference = null;
        private String scope = Constants.SCOPE;
        private String accountId = null;
        private ConnectCloseCallback onClose = null;
        private ConnectEventCallback onEvent = null;
        private MonoInstitution selectedInstitution = null;
        private MonoCustomer customer = null;

        public Builder(Context context, String publicKey, ConnectSuccessCallback onSuccess) {
            this.context = context;
            this.publicKey = publicKey;
            this.onSuccess = onSuccess;
        }

        public Builder addReference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder addScope(String scope) {
            this.scope = scope;
            return this;
        }

        public Builder addAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder addOnClose(ConnectCloseCallback onClose) {
            this.onClose = onClose;
            return this;
        }

        public Builder addOnEvent(ConnectEventCallback onEvent) {
            this.onEvent = onEvent;
            return this;
        }

        public Builder addSelectedInstitution(MonoInstitution selectedInstitution) {
            this.selectedInstitution = selectedInstitution;
            return this;
        }

        public Builder addCustomer(MonoCustomer customer) {
            this.customer = customer;
            return this;
        }

        public MonoConfiguration build() {
            return new MonoConfiguration(this);
        }
    }
}