package io.ont.orangedpexample.controller;

import com.alibaba.fastjson.JSON;

import com.github.ontio.common.Helper;
import io.ont.entity.OrangeProviderOntSdk;
import io.ont.orangedpexample.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
@Slf4j
public class DPController {
    @GetMapping("/ping")
    public DefaultResp checkMessage() {
        DefaultResp pong = DefaultResp.builder().message("pong").build();
        return pong;
    }

    String selfDID = "did:ont:Af3r35XWVCmnXRfAkGEs2vSGfeZhS24NoT";

    @PostMapping("/balance")
    public Result DPRequest(@RequestBody BalanceReq requestJson) throws Exception {
        System.out.println(requestJson.getEncrypt());
        System.out.println(requestJson.getChain());
        OrangeProviderOntSdk orangeProviderOntSdk = OrangeProviderOntSdk.getOrangeProviderOntSdk("testnet", "./wallet.dat", "passwordtest");
        System.out.println("address: " + requestJson.getAddress());
        System.out.println("chain: " + requestJson.getChain());
        System.out.println("encrypt: " + requestJson.getEncrypt());

        byte[] didPubkey = orangeProviderOntSdk.getDIDPubkey(orangeProviderOntSdk.getSelfDID());
        System.out.println(" did Pubkey is  "+Helper.toHexString(didPubkey));

        BalanceData balanceData = new BalanceData();
        balanceData.setBalance("1000000");

        String jsonString = JSON.toJSONString(balanceData);
        byte[] dataToSign = jsonString.getBytes();
        byte[] sig = orangeProviderOntSdk.signData(dataToSign);

        // 签名完成的数据
        String sigString = Helper.toHexString(sig);
        System.out.println(sigString);

        RespData dataWithSig = new RespData();
        dataWithSig.setData(balanceData);
        dataWithSig.setSig(sigString);

        if (requestJson.getEncrypt()) {
            byte[] databytes = JSON.toJSONString(dataWithSig).getBytes();
            String userDID = requestJson.getUserDID();

            byte[] dataWithencrpyed = orangeProviderOntSdk.encryptDataWithDID(databytes, userDID);
            System.out.println("Msg:" + Helper.toHexString(databytes));
            return Result.builder().provider_did(selfDID).encrypted(Helper.toHexString(dataWithencrpyed)).build();
        }else {
            return Result.builder().provider_did(selfDID).data(dataWithSig).build();
        }
    }

}








