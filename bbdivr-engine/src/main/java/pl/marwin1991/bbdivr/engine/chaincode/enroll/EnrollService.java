package pl.marwin1991.bbdivr.engine.chaincode.enroll;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.marwin1991.bbdivr.engine.chaincode.wallet.WalletService;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.security.cert.CertificateException;
import java.util.Properties;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EnrollService {

    public final static String NETWORK_CONFIG_PATH = "bbdivr-network/organizations/peerOrganizations/org1.example.com/connection-org1.yaml";
    public final static String APP_USER = "bbdivr-user";
    public final static String ADMIN_USER = "admin";
    private final static String PEM_FILE_PATH = "bbdivr-network/organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem";
    private final static String ORG1_CA_URL = "https://localhost:7054";
    private final WalletService walletService;

    public void enrollAdmin() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, CryptoException, InvalidArgumentException, CertificateException, EnrollmentException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException {
        HFCAClient caClient = getCaClient();
        Wallet wallet = walletService.getWallet();

        if (wallet.get(ADMIN_USER) != null) {
            log.info("An identity for the admin user \"" + ADMIN_USER + "\" already exists in the wallet");
            return;
        }

        // Enroll the admin user, and import the new identity into the wallet.
        Enrollment enrollment = getAdminEnrollment(caClient);
        Identity user = Identities.newX509Identity("Org1MSP", enrollment);
        wallet.put(ADMIN_USER, user);

        log.info("Successfully enrolled user \"" + ADMIN_USER + "\" and imported it into the wallet");
    }

    public boolean isEnrolled() throws IOException {
        Wallet wallet = walletService.getWallet();
        return wallet.get(APP_USER) != null;
    }

    public void registerUser() throws Exception {
        HFCAClient caClient = getCaClient();
        Wallet wallet = walletService.getWallet();

        // Check to see if we've already enrolled the user.
        if (wallet.get(APP_USER) != null) {
            log.info("An identity for the user \"" + APP_USER + "\" already exists in the wallet");
            return;
        }


        if (wallet.get(ADMIN_USER) == null) {
            throw new RuntimeException(ADMIN_USER + "\" needs to be enrolled and added to the wallet first");
        }

        Identity adminIdentity = wallet.get(ADMIN_USER);
        User admin = getNewUser(getAdminEnrollment(caClient), adminIdentity.getMspId());

        // Register the user, enroll the user, and import the new identity into the wallet.
        RegistrationRequest registrationRequest = new RegistrationRequest(APP_USER);
        registrationRequest.setAffiliation("org1.department1");
        registrationRequest.setEnrollmentID(APP_USER);
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        Enrollment enrollment = caClient.enroll(APP_USER, enrollmentSecret);
        Identity user = Identities.newX509Identity("Org1MSP", enrollment);
        wallet.put(APP_USER, user);
        log.info("Successfully enrolled user \"" + APP_USER + "\" and imported it into the wallet");
    }


    //Create a CA client for interacting with the CA.
    private HFCAClient getCaClient() throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, CryptoException, InvalidArgumentException {
        Properties props = new Properties();
        props.put("pemFile", PEM_FILE_PATH);
        props.put("allowAllHostNames", "true");
        HFCAClient caClient = HFCAClient.createNewInstance(ORG1_CA_URL, props);
        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
        caClient.setCryptoSuite(cryptoSuite);

        return caClient;
    }

    private Enrollment getAdminEnrollment(HFCAClient caClient) throws EnrollmentException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException {
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost("localhost");
        enrollmentRequestTLS.setProfile("tls");
        return caClient.enroll(ADMIN_USER, "adminpw", enrollmentRequestTLS);
    }

    private User getNewUser(Enrollment enrollment, String mspId) {
        return new User() {

            @Override
            public String getName() {
                return APP_USER;
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return "org1.department1";
            }

            @Override
            public Enrollment getEnrollment() {
                return enrollment;
            }

            @Override
            public String getMspId() {
                return mspId;
            }

        };
    }
}
