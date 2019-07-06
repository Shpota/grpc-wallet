package com.sashashpota.wallet.controllers;

import com.sashashpota.wallet.services.WalletException;
import com.sashashpota.wallet.services.WalletService;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.grpc.Status.INVALID_ARGUMENT;

@GRpcService
class WalletController extends WalletGrpc.WalletImplBase {
    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    private final WalletService service;

    WalletController(WalletService service) {
        this.service = service;
    }

    @Override
    public void balance(BalanceRequest request, StreamObserver<BalanceReply> responseObserver) {
        String userId = request.getUserId();
        BalanceReply.Builder builder = BalanceReply.newBuilder();
        service.getBalances(userId).forEach(
                (currency, amount) -> builder.addBalance(
                        Balance.newBuilder().setCurrency(currency).setAmount(amount).build()
                )
        );
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void deposit(ChangeBalanceRequest request, StreamObserver<Empty> responseObserver) {
        try {
            service.deposit(request.getUserId(), request.getCurrency(), request.getAmount());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (WalletException e) {
            logger.warn(e.getMessage(), e);
            responseObserver.onError(new StatusRuntimeException(INVALID_ARGUMENT));
        }
    }

    @Override
    public void withdraw(ChangeBalanceRequest request, StreamObserver<Empty> responseObserver) {
        try {
            service.withdraw(request.getUserId(), request.getCurrency(), request.getAmount());
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (WalletException e) {
            logger.warn(e.getMessage(), e);
            responseObserver.onError(new StatusRuntimeException(INVALID_ARGUMENT));
        }
    }
}
