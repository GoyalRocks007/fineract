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

import static org.apache.fineract.portfolio.savings.DepositsApiConstants.annualInterestRateParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.interestCompoundingPeriodInMonthsParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.principalAmountParamName;
import static org.apache.fineract.portfolio.savings.DepositsApiConstants.tenureInMonthsParamName;

import com.google.gson.JsonElement;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.apache.fineract.infrastructure.core.api.JsonQuery;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.portfolio.savings.data.DepositAccountDataValidator;

@RequiredArgsConstructor
public class FixedDepositInterestCalculationServiceImpl implements FixedDepositInterestCalculationService {

    private final DepositAccountDataValidator depositAccountDataValidator;
    private final FromJsonHelper fromApiJsonHelper;

    @Override
    public double calculateInterest(JsonQuery query) {
        depositAccountDataValidator.validateFixedDepositForInterestCalculation(query.json());
        JsonElement element = query.parsedJson();
        Long principalAmount = this.fromApiJsonHelper.extractLongNamed(principalAmountParamName, element);
        BigDecimal annualInterestRate = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed(annualInterestRateParamName, element);
        Long tenureInMonths = this.fromApiJsonHelper.extractLongNamed(tenureInMonthsParamName, element);
        Long interestCompoundingPeriodInMonths = this.fromApiJsonHelper.extractLongNamed(interestCompoundingPeriodInMonthsParamName,
                element);

        double n = (12 / (double) interestCompoundingPeriodInMonths);
        double r = annualInterestRate.doubleValue();
        return principalAmount * Math.pow((1 + (r / (n * 100))), ((tenureInMonths * n) / 12));
    }
}
