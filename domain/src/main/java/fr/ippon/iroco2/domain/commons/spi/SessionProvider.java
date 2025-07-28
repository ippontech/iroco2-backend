package fr.ippon.iroco2.domain.commons.spi;

public interface SessionProvider {
    String getConnectedAwsAccountId();
    String getConnectedUserEmail();
}
