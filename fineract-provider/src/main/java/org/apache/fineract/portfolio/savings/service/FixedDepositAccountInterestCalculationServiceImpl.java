/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.fineract.portfolio.savings.service;

import com.google.gson.JsonElement;
import lombok.RequiredArgsConstructor;
import org.apache.fineract.infrastructure.core.api.JsonQuery;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.portfolio.savings.data.DepositAccountDataValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FixedDepositAccountInterestCalculationServiceImpl implements FixedDepositAccountInterestCalculationService{

    private final DepositAccountDataValidator depositAccountDataValidator;
    private final FromJsonHelper fromApiJsonHelper;
    @Override
    public double calculateInterest(JsonQuery query) {
        depositAccountDataValidator.validateFixedDepositForInterestCalculation(query.json());
        JsonElement element = query.parsedJson();
        Long principalAmount = this.fromApiJsonHelper.extractLongNamed("principalAmount", element);
        BigDecimal annualInterestRate = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("annualInterestRate", element);
        Long tenureInMonths = this.fromApiJsonHelper.extractLongNamed("tenureInMonths", element);
        Long interestPostingPeriodInMonths = this.fromApiJsonHelper.extractLongNamed("interestPostingPeriodInMonths", element);
        Long interestCompoundingPeriodInMonths = this.fromApiJsonHelper.extractLongNamed("interestCompoundingPeriodInMonths", element);

        double n = (12/(double)interestCompoundingPeriodInMonths);
        double r = annualInterestRate.doubleValue();
        double maturityAmount =principalAmount * (Math.pow((1+(r/(n*100))),((tenureInMonths*n)/12)));
        return maturityAmount;
    }
}
