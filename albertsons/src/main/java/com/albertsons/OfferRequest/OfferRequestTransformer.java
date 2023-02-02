package com.albertsons.OfferRequest;

public class OfferRequestTransformer {

    public String transform(String json) {
        StringBuilder builder = new StringBuilder();
        GetOfferRequest_DocumentData();
        GetOfferRequest_OfferRequestData();
        return builder.toString();
    }

    // GetOfferRequest/DocumentData
    protected void GetOfferRequest_DocumentData() {
        GetOfferRequest_DocumentData_Document();
        GetOfferRequest_DocumentData_DocumentAction();
    }

    // GetOfferRequest/DocumentData/Document
    protected void GetOfferRequest_DocumentData_Document() {
        GetOfferRequest_DocumentData_Document_ReleaseId();
        GetOfferRequest_DocumentData_Document_VersionId();
        GetOfferRequest_DocumentData_Document_SystemEnvironmentCd();
        GetOfferRequest_DocumentData_Document_DocumentID();
        GetOfferRequest_DocumentData_Document_AlternateDocumentID();
        GetOfferRequest_DocumentData_Document_InboundOutboundInd();
        GetOfferRequest_DocumentData_Document_DocumentNm();
        GetOfferRequest_DocumentData_Document_CreationDt();
        GetOfferRequest_DocumentData_Document_Description();
        GetOfferRequest_DocumentData_Document_SourceApplicationCd();
        GetOfferRequest_DocumentData_Document_TargetApplicationCd();
        GetOfferRequest_DocumentData_Document_Note();
        GetOfferRequest_DocumentData_Document_GatewayNm();
        GetOfferRequest_DocumentData_Document_SenderId();
        GetOfferRequest_DocumentData_Document_ReceiverId();
        GetOfferRequest_DocumentData_Document_RoutingSystemNm();
        GetOfferRequest_DocumentData_Document_InternalFileTransferInd();
        GetOfferRequest_DocumentData_Document_InterchangeDate();
        GetOfferRequest_DocumentData_Document_InterchangeTime();
        GetOfferRequest_DocumentData_Document_ExternalTargetInd();
        GetOfferRequest_DocumentData_Document_MessageSequenceNbr();
        GetOfferRequest_DocumentData_Document_ExpectedMessageCnt();
        GetOfferRequest_DocumentData_Document_DataClassification();
    }

    // GetOfferRequest/DocumentData/Document/@ReleaseId
    protected void GetOfferRequest_DocumentData_Document_ReleaseId() {
    }

    // GetOfferRequest/DocumentData/Document/@VersionId
    protected void GetOfferRequest_DocumentData_Document_VersionId() {
    }

    // GetOfferRequest/DocumentData/Document/@SystemEnvironmentCd
    protected void GetOfferRequest_DocumentData_Document_SystemEnvironmentCd() {
    }

    // GetOfferRequest/DocumentData/Document/DocumentID
    protected void GetOfferRequest_DocumentData_Document_DocumentID() {
    }

    // GetOfferRequest/DocumentData/Document/AlternateDocumentID
    protected void GetOfferRequest_DocumentData_Document_AlternateDocumentID() {
    }

    // GetOfferRequest/DocumentData/Document/InboundOutboundInd
    protected void GetOfferRequest_DocumentData_Document_InboundOutboundInd() {
    }

    // GetOfferRequest/DocumentData/Document/DocumentNm
    protected void GetOfferRequest_DocumentData_Document_DocumentNm() {
    }

    // GetOfferRequest/DocumentData/Document/CreationDt
    protected void GetOfferRequest_DocumentData_Document_CreationDt() {
    }

    // GetOfferRequest/DocumentData/Document/Description
    protected void GetOfferRequest_DocumentData_Document_Description() {
    }

    // GetOfferRequest/DocumentData/Document/SourceApplicationCd
    protected void GetOfferRequest_DocumentData_Document_SourceApplicationCd() {
    }

    // GetOfferRequest/DocumentData/Document/TargetApplicationCd
    protected void GetOfferRequest_DocumentData_Document_TargetApplicationCd() {
    }

    // GetOfferRequest/DocumentData/Document/Note
    protected void GetOfferRequest_DocumentData_Document_Note() {
    }

    // GetOfferRequest/DocumentData/Document/GatewayNm
    protected void GetOfferRequest_DocumentData_Document_GatewayNm() {
    }

    // GetOfferRequest/DocumentData/Document/SenderId
    protected void GetOfferRequest_DocumentData_Document_SenderId() {
    }

    // GetOfferRequest/DocumentData/Document/ReceiverId
    protected void GetOfferRequest_DocumentData_Document_ReceiverId() {
    }

    // GetOfferRequest/DocumentData/Document/RoutingSystemNm
    protected void GetOfferRequest_DocumentData_Document_RoutingSystemNm() {
    }

    // GetOfferRequest/DocumentData/Document/InternalFileTransferInd
    protected void GetOfferRequest_DocumentData_Document_InternalFileTransferInd() {
    }

    // GetOfferRequest/DocumentData/Document/InterchangeDate
    protected void GetOfferRequest_DocumentData_Document_InterchangeDate() {
    }

    // GetOfferRequest/DocumentData/Document/InterchangeTime
    protected void GetOfferRequest_DocumentData_Document_InterchangeTime() {
    }

    // GetOfferRequest/DocumentData/Document/ExternalTargetInd
    protected void GetOfferRequest_DocumentData_Document_ExternalTargetInd() {
    }

    // GetOfferRequest/DocumentData/Document/MessageSequenceNbr
    protected void GetOfferRequest_DocumentData_Document_MessageSequenceNbr() {
    }

    // GetOfferRequest/DocumentData/Document/ExpectedMessageCnt
    protected void GetOfferRequest_DocumentData_Document_ExpectedMessageCnt() {
    }

    // GetOfferRequest/DocumentData/Document/DataClassification
    protected void GetOfferRequest_DocumentData_Document_DataClassification() {
        GetOfferRequest_DocumentData_Document_DataClassification_DataClassificationLevel();
        GetOfferRequest_DocumentData_Document_DataClassification_BusinessSensitivityLevel();
        GetOfferRequest_DocumentData_Document_DataClassification_PHIdataInd();
        GetOfferRequest_DocumentData_Document_DataClassification_PCIdataInd();
        GetOfferRequest_DocumentData_Document_DataClassification_PIIdataInd();
    }

    // GetOfferRequest/DocumentData/Document/DataClassification/DataClassificationLevel
    protected void GetOfferRequest_DocumentData_Document_DataClassification_DataClassificationLevel() {
        GetOfferRequest_DocumentData_Document_DataClassification_DataClassificationLevel_Code();
        GetOfferRequest_DocumentData_Document_DataClassification_DataClassificationLevel_Description();
        GetOfferRequest_DocumentData_Document_DataClassification_DataClassificationLevel_ShortDescription();
    }

    // GetOfferRequest/DocumentData/Document/DataClassification/DataClassificationLevel/Code
    protected void GetOfferRequest_DocumentData_Document_DataClassification_DataClassificationLevel_Code() {
    }

    // GetOfferRequest/DocumentData/Document/DataClassification/DataClassificationLevel/Description
    protected void GetOfferRequest_DocumentData_Document_DataClassification_DataClassificationLevel_Description() {
    }

    // GetOfferRequest/DocumentData/Document/DataClassification/DataClassificationLevel/ShortDescription
    protected void GetOfferRequest_DocumentData_Document_DataClassification_DataClassificationLevel_ShortDescription() {
    }

    // GetOfferRequest/DocumentData/Document/DataClassification/BusinessSensitivityLevel
    protected void GetOfferRequest_DocumentData_Document_DataClassification_BusinessSensitivityLevel() {
        GetOfferRequest_DocumentData_Document_DataClassification_BusinessSensitivityLevel_Code();
        GetOfferRequest_DocumentData_Document_DataClassification_BusinessSensitivityLevel_Description();
        GetOfferRequest_DocumentData_Document_DataClassification_BusinessSensitivityLevel_ShortDescription();
    }

    // GetOfferRequest/DocumentData/Document/DataClassification/BusinessSensitivityLevel/Code
    protected void GetOfferRequest_DocumentData_Document_DataClassification_BusinessSensitivityLevel_Code() {
    }

    // GetOfferRequest/DocumentData/Document/DataClassification/BusinessSensitivityLevel/Description
    protected void GetOfferRequest_DocumentData_Document_DataClassification_BusinessSensitivityLevel_Description() {
    }

    // GetOfferRequest/DocumentData/Document/DataClassification/BusinessSensitivityLevel/ShortDescription
    protected void GetOfferRequest_DocumentData_Document_DataClassification_BusinessSensitivityLevel_ShortDescription() {
    }

    // GetOfferRequest/DocumentData/Document/DataClassification/PHIdataInd
    protected void GetOfferRequest_DocumentData_Document_DataClassification_PHIdataInd() {
    }

    // GetOfferRequest/DocumentData/Document/DataClassification/PCIdataInd
    protected void GetOfferRequest_DocumentData_Document_DataClassification_PCIdataInd() {
    }

    // GetOfferRequest/DocumentData/Document/DataClassification/PIIdataInd
    protected void GetOfferRequest_DocumentData_Document_DataClassification_PIIdataInd() {
    }

    // GetOfferRequest/DocumentData/DocumentAction
    protected void GetOfferRequest_DocumentData_DocumentAction() {
        GetOfferRequest_DocumentData_DocumentAction_ActionTypeCd();
        GetOfferRequest_DocumentData_DocumentAction_RecordTypeCd();
    }

    // GetOfferRequest/DocumentData/DocumentAction/ActionTypeCd
    protected void GetOfferRequest_DocumentData_DocumentAction_ActionTypeCd() {
    }

    // GetOfferRequest/DocumentData/DocumentAction/RecordTypeCd
    protected void GetOfferRequest_DocumentData_DocumentAction_RecordTypeCd() {
    }

    // GetOfferRequest/OfferRequestData
    protected void GetOfferRequest_OfferRequestData() {
        GetOfferRequest_OfferRequestData_OfferRequestId();
        GetOfferRequest_OfferRequestData_OfferOrganization();
        GetOfferRequest_OfferRequestData_OfferDeliveryChannelType();
        GetOfferRequest_OfferRequestData_OfferFulfillmentChannel();
        GetOfferRequest_OfferRequestData_OfferBank();
        GetOfferRequest_OfferRequestData_OfferRequestTemplate();
        GetOfferRequest_OfferRequestData_OfferRequestTemplateStatus();
        GetOfferRequest_OfferRequestData_DepartmentId();
        GetOfferRequest_OfferRequestData_DepartmentNm();
        GetOfferRequest_OfferRequestData_OfferNm();
        GetOfferRequest_OfferRequestData_OfferRequestDsc();
        GetOfferRequest_OfferRequestData_BrandInfoTxt();
        GetOfferRequest_OfferRequestData_SizeDsc();
        GetOfferRequest_OfferRequestData_CustomerSegmentInfoTxt();
        GetOfferRequest_OfferRequestData_OfferItemDsc();
        GetOfferRequest_OfferRequestData_OfferFlagDsc();
        GetOfferRequest_OfferRequestData_OfferRequestStatus();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail();
        GetOfferRequest_OfferRequestData_VendorPromotionType();
        GetOfferRequest_OfferRequestData_PromotionProgram();
        GetOfferRequest_OfferRequestData_PromotionProgramType();
        GetOfferRequest_OfferRequestData_PromotionPeriodType();
        GetOfferRequest_OfferRequestData_AdvertisementType();
        GetOfferRequest_OfferRequestData_TriggerId();
        GetOfferRequest_OfferRequestData_SavingsValueTxt();
        GetOfferRequest_OfferRequestData_DisclaimerTxt();
        GetOfferRequest_OfferRequestData_ImageId();
        GetOfferRequest_OfferRequestData_OfferPeriodType();
        GetOfferRequest_OfferRequestData_OfferRequestSource();
        GetOfferRequest_OfferRequestData_OfferEffectiveDay();
        GetOfferRequest_OfferRequestData_OfferEffectiveTm();
        GetOfferRequest_OfferRequestData_OfferRestrictionType();
        GetOfferRequest_OfferRequestData_RequirementType();
        GetOfferRequest_OfferRequestData_OfferRequestReferenceId();
        GetOfferRequest_OfferRequestData_ManufacturerType();
        GetOfferRequest_OfferRequestData_AttachmentType();
        GetOfferRequest_OfferRequestData_OfferRequestTypeCd();
        GetOfferRequest_OfferRequestData_AllocationTypeCd();
        GetOfferRequest_OfferRequestData_BusinessJustificationTxt();
        GetOfferRequest_OfferRequestData_OfferRequestCommentTxt();
        GetOfferRequest_OfferRequestData_AttachedOfferType();
        GetOfferRequest_OfferRequestData_VersionQty();
        GetOfferRequest_OfferRequestData_TierQty();
        GetOfferRequest_OfferRequestData_ProductQty();
        GetOfferRequest_OfferRequestData_StoreGroupQty();
        GetOfferRequest_OfferRequestData_DeletedOfferType();
        GetOfferRequest_OfferRequestData_ChargeBackDepartment();
        GetOfferRequest_OfferRequestData_OfferReviewChecklist();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestId
    protected void GetOfferRequest_OfferRequestData_OfferRequestId() {
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization
    protected void GetOfferRequest_OfferRequestData_OfferOrganization() {
        GetOfferRequest_OfferRequestData_OfferOrganization_Group();
        GetOfferRequest_OfferRequestData_OfferOrganization_OfferRegion();
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization/Group
    protected void GetOfferRequest_OfferRequestData_OfferOrganization_Group() {
        GetOfferRequest_OfferRequestData_OfferOrganization_Group_GroupId();
        GetOfferRequest_OfferRequestData_OfferOrganization_Group_GroupCd();
        GetOfferRequest_OfferRequestData_OfferOrganization_Group_GroupNm();
        GetOfferRequest_OfferRequestData_OfferOrganization_Group_SubGroup();
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization/Group/GroupId
    protected void GetOfferRequest_OfferRequestData_OfferOrganization_Group_GroupId() {
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization/Group/GroupCd
    protected void GetOfferRequest_OfferRequestData_OfferOrganization_Group_GroupCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization/Group/GroupNm
    protected void GetOfferRequest_OfferRequestData_OfferOrganization_Group_GroupNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization/Group/SubGroup
    protected void GetOfferRequest_OfferRequestData_OfferOrganization_Group_SubGroup() {
        GetOfferRequest_OfferRequestData_OfferOrganization_Group_SubGroup_SubGroupId();
        GetOfferRequest_OfferRequestData_OfferOrganization_Group_SubGroup_SubGroupCd();
        GetOfferRequest_OfferRequestData_OfferOrganization_Group_SubGroup_SubGroupNm();
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization/Group/SubGroup/SubGroupId
    protected void GetOfferRequest_OfferRequestData_OfferOrganization_Group_SubGroup_SubGroupId() {
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization/Group/SubGroup/SubGroupCd
    protected void GetOfferRequest_OfferRequestData_OfferOrganization_Group_SubGroup_SubGroupCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization/Group/SubGroup/SubGroupNm
    protected void GetOfferRequest_OfferRequestData_OfferOrganization_Group_SubGroup_SubGroupNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization/OfferRegion
    protected void GetOfferRequest_OfferRequestData_OfferOrganization_OfferRegion() {
        GetOfferRequest_OfferRequestData_OfferOrganization_OfferRegion_RegionId();
        GetOfferRequest_OfferRequestData_OfferOrganization_OfferRegion_RegionNm();
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization/OfferRegion/RegionId
    protected void GetOfferRequest_OfferRequestData_OfferOrganization_OfferRegion_RegionId() {
    }

    // GetOfferRequest/OfferRequestData/OfferOrganization/OfferRegion/RegionNm
    protected void GetOfferRequest_OfferRequestData_OfferOrganization_OfferRegion_RegionNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferDeliveryChannelType
    protected void GetOfferRequest_OfferRequestData_OfferDeliveryChannelType() {
        GetOfferRequest_OfferRequestData_OfferDeliveryChannelType_DeliveryChannelTypeCd();
        GetOfferRequest_OfferRequestData_OfferDeliveryChannelType_DeliveryChannelTypeDsc();
    }

    // GetOfferRequest/OfferRequestData/OfferDeliveryChannelType/DeliveryChannelTypeCd
    protected void GetOfferRequest_OfferRequestData_OfferDeliveryChannelType_DeliveryChannelTypeCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferDeliveryChannelType/DeliveryChannelTypeDsc
    protected void GetOfferRequest_OfferRequestData_OfferDeliveryChannelType_DeliveryChannelTypeDsc() {
    }

    // GetOfferRequest/OfferRequestData/OfferFulfillmentChannel
    protected void GetOfferRequest_OfferRequestData_OfferFulfillmentChannel() {
        GetOfferRequest_OfferRequestData_OfferFulfillmentChannel_FulfillmentChannelTypeCd();
        GetOfferRequest_OfferRequestData_OfferFulfillmentChannel_FulfillmentChannelTypeDesc();
        GetOfferRequest_OfferRequestData_OfferFulfillmentChannel_FulfillmentChannelInd();
    }

    // GetOfferRequest/OfferRequestData/OfferFulfillmentChannel/FulfillmentChannelTypeCd
    protected void GetOfferRequest_OfferRequestData_OfferFulfillmentChannel_FulfillmentChannelTypeCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferFulfillmentChannel/FulfillmentChannelTypeDesc
    protected void GetOfferRequest_OfferRequestData_OfferFulfillmentChannel_FulfillmentChannelTypeDesc() {
    }

    // GetOfferRequest/OfferRequestData/OfferFulfillmentChannel/FulfillmentChannelInd
    protected void GetOfferRequest_OfferRequestData_OfferFulfillmentChannel_FulfillmentChannelInd() {
    }

    // GetOfferRequest/OfferRequestData/OfferBank
    protected void GetOfferRequest_OfferRequestData_OfferBank() {
        GetOfferRequest_OfferRequestData_OfferBank_OfferBankTypeCd();
        GetOfferRequest_OfferRequestData_OfferBank_OfferBankId();
        GetOfferRequest_OfferRequestData_OfferBank_OfferBankNm();
    }

    // GetOfferRequest/OfferRequestData/OfferBank/OfferBankTypeCd
    protected void GetOfferRequest_OfferRequestData_OfferBank_OfferBankTypeCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferBank/OfferBankId
    protected void GetOfferRequest_OfferRequestData_OfferBank_OfferBankId() {
    }

    // GetOfferRequest/OfferRequestData/OfferBank/OfferBankNm
    protected void GetOfferRequest_OfferRequestData_OfferBank_OfferBankNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestTemplate
    protected void GetOfferRequest_OfferRequestData_OfferRequestTemplate() {
        GetOfferRequest_OfferRequestData_OfferRequestTemplate_TemplateId();
        GetOfferRequest_OfferRequestData_OfferRequestTemplate_TemplateNm();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestTemplate/TemplateId
    protected void GetOfferRequest_OfferRequestData_OfferRequestTemplate_TemplateId() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestTemplate/TemplateNm
    protected void GetOfferRequest_OfferRequestData_OfferRequestTemplate_TemplateNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestTemplateStatus
    protected void GetOfferRequest_OfferRequestData_OfferRequestTemplateStatus() {
        GetOfferRequest_OfferRequestData_OfferRequestTemplateStatus_StatusTypeCd();
        GetOfferRequest_OfferRequestData_OfferRequestTemplateStatus_Description();
        GetOfferRequest_OfferRequestData_OfferRequestTemplateStatus_EffectiveDtTm();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestTemplateStatus/StatusTypeCd
    protected void GetOfferRequest_OfferRequestData_OfferRequestTemplateStatus_StatusTypeCd() {
        GetOfferRequest_OfferRequestData_OfferRequestTemplateStatus_StatusTypeCd_Type();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestTemplateStatus/StatusTypeCd/@Type
    protected void GetOfferRequest_OfferRequestData_OfferRequestTemplateStatus_StatusTypeCd_Type() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestTemplateStatus/Description
    protected void GetOfferRequest_OfferRequestData_OfferRequestTemplateStatus_Description() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestTemplateStatus/EffectiveDtTm
    protected void GetOfferRequest_OfferRequestData_OfferRequestTemplateStatus_EffectiveDtTm() {
    }

    // GetOfferRequest/OfferRequestData/DepartmentId
    protected void GetOfferRequest_OfferRequestData_DepartmentId() {
    }

    // GetOfferRequest/OfferRequestData/DepartmentNm
    protected void GetOfferRequest_OfferRequestData_DepartmentNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferNm
    protected void GetOfferRequest_OfferRequestData_OfferNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestDsc
    protected void GetOfferRequest_OfferRequestData_OfferRequestDsc() {
    }

    // GetOfferRequest/OfferRequestData/BrandInfoTxt
    protected void GetOfferRequest_OfferRequestData_BrandInfoTxt() {
    }

    // GetOfferRequest/OfferRequestData/SizeDsc
    protected void GetOfferRequest_OfferRequestData_SizeDsc() {
    }

    // GetOfferRequest/OfferRequestData/CustomerSegmentInfoTxt
    protected void GetOfferRequest_OfferRequestData_CustomerSegmentInfoTxt() {
    }

    // GetOfferRequest/OfferRequestData/OfferItemDsc
    protected void GetOfferRequest_OfferRequestData_OfferItemDsc() {
    }

    // GetOfferRequest/OfferRequestData/OfferFlagDsc
    protected void GetOfferRequest_OfferRequestData_OfferFlagDsc() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestStatus
    protected void GetOfferRequest_OfferRequestData_OfferRequestStatus() {
        GetOfferRequest_OfferRequestData_OfferRequestStatus_StatusTypeCd();
        GetOfferRequest_OfferRequestData_OfferRequestStatus_Description();
        GetOfferRequest_OfferRequestData_OfferRequestStatus_EffectiveDtTm();
        GetOfferRequest_OfferRequestData_OfferRequestStatus_OfferRequestStatusTypeCd();
        GetOfferRequest_OfferRequestData_OfferRequestStatus_TimeZoneCd();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestStatus/StatusTypeCd
    protected void GetOfferRequest_OfferRequestData_OfferRequestStatus_StatusTypeCd() {
        GetOfferRequest_OfferRequestData_OfferRequestStatus_StatusTypeCd_Type();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestStatus/StatusTypeCd/@Type
    protected void GetOfferRequest_OfferRequestData_OfferRequestStatus_StatusTypeCd_Type() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestStatus/Description
    protected void GetOfferRequest_OfferRequestData_OfferRequestStatus_Description() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestStatus/EffectiveDtTm
    protected void GetOfferRequest_OfferRequestData_OfferRequestStatus_EffectiveDtTm() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestStatus/OfferRequestStatusTypeCd
    protected void GetOfferRequest_OfferRequestData_OfferRequestStatus_OfferRequestStatusTypeCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestStatus/TimeZoneCd
    protected void GetOfferRequest_OfferRequestData_OfferRequestStatus_TimeZoneCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail() {
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeType();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeCategory();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ReasonType();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeByType();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeType
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeType() {
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeType_ChangeTypeCd();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeType_ChangeTypeDsc();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeType_ChangeTypeQty();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeType/ChangeTypeCd
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeType_ChangeTypeCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeType/ChangeTypeDsc
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeType_ChangeTypeDsc() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeType/ChangeTypeQty
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeType_ChangeTypeQty() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeCategory
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeCategory() {
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeCategory_ChangeCategoryCd();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeCategory_ChangeCategoryDsc();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeCategory_ChangeCategoryQty();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeCategory/ChangeCategoryCd
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeCategory_ChangeCategoryCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeCategory/ChangeCategoryDsc
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeCategory_ChangeCategoryDsc() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeCategory/ChangeCategoryQty
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeCategory_ChangeCategoryQty() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ReasonType
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ReasonType() {
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ReasonType_ReasonTypeCd();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ReasonType_ReasonTypeDsc();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ReasonType_CommentTxt();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ReasonType/ReasonTypeCd
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ReasonType_ReasonTypeCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ReasonType/ReasonTypeDsc
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ReasonType_ReasonTypeDsc() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ReasonType/CommentTxt
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ReasonType_CommentTxt() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeByType
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeByType() {
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeByType_UserId();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeByType_FirstNm();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeByType_LastNm();
        GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeByType_ChangeByDtTm();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeByType/UserId
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeByType_UserId() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeByType/FirstNm
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeByType_FirstNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeByType/LastNm
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeByType_LastNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestChangeDetail/ChangeByType/ChangeByDtTm
    protected void GetOfferRequest_OfferRequestData_OfferRequestChangeDetail_ChangeByType_ChangeByDtTm() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType() {
        GetOfferRequest_OfferRequestData_VendorPromotionType_VendorPromotionId();
        GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAStartDt();
        GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAEndDt();
        GetOfferRequest_OfferRequestData_VendorPromotionType_BillingOptionType();
        GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAAssignStatus();
        GetOfferRequest_OfferRequestData_VendorPromotionType_AllowanceType();
        GetOfferRequest_OfferRequestData_VendorPromotionType_BilledInd();
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/VendorPromotionId
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_VendorPromotionId() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/NOPAStartDt
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAStartDt() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/NOPAEndDt
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAEndDt() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/BillingOptionType
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_BillingOptionType() {
        GetOfferRequest_OfferRequestData_VendorPromotionType_BillingOptionType_Code();
        GetOfferRequest_OfferRequestData_VendorPromotionType_BillingOptionType_Description();
        GetOfferRequest_OfferRequestData_VendorPromotionType_BillingOptionType_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/BillingOptionType/Code
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_BillingOptionType_Code() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/BillingOptionType/Description
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_BillingOptionType_Description() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/BillingOptionType/ShortDescription
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_BillingOptionType_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/NOPAAssignStatus
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAAssignStatus() {
        GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAAssignStatus_StatusTypeCd();
        GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAAssignStatus_Description();
        GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAAssignStatus_EffectiveDtTm();
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/NOPAAssignStatus/StatusTypeCd
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAAssignStatus_StatusTypeCd() {
        GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAAssignStatus_StatusTypeCd_Type();
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/NOPAAssignStatus/StatusTypeCd/@Type
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAAssignStatus_StatusTypeCd_Type() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/NOPAAssignStatus/Description
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAAssignStatus_Description() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/NOPAAssignStatus/EffectiveDtTm
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_NOPAAssignStatus_EffectiveDtTm() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/AllowanceType
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_AllowanceType() {
        GetOfferRequest_OfferRequestData_VendorPromotionType_AllowanceType_Code();
        GetOfferRequest_OfferRequestData_VendorPromotionType_AllowanceType_Description();
        GetOfferRequest_OfferRequestData_VendorPromotionType_AllowanceType_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/AllowanceType/Code
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_AllowanceType_Code() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/AllowanceType/Description
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_AllowanceType_Description() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/AllowanceType/ShortDescription
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_AllowanceType_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/VendorPromotionType/BilledInd
    protected void GetOfferRequest_OfferRequestData_VendorPromotionType_BilledInd() {
    }

    // GetOfferRequest/OfferRequestData/PromotionProgram
    protected void GetOfferRequest_OfferRequestData_PromotionProgram() {
        GetOfferRequest_OfferRequestData_PromotionProgram_Code();
        GetOfferRequest_OfferRequestData_PromotionProgram_Name();
    }

    // GetOfferRequest/OfferRequestData/PromotionProgram/Code
    protected void GetOfferRequest_OfferRequestData_PromotionProgram_Code() {
    }

    // GetOfferRequest/OfferRequestData/PromotionProgram/Name
    protected void GetOfferRequest_OfferRequestData_PromotionProgram_Name() {
    }

    // GetOfferRequest/OfferRequestData/PromotionProgramType
    protected void GetOfferRequest_OfferRequestData_PromotionProgramType() {
        GetOfferRequest_OfferRequestData_PromotionProgramType_Code();
        GetOfferRequest_OfferRequestData_PromotionProgramType_Name();
        GetOfferRequest_OfferRequestData_PromotionProgramType_ProgramSubType();
    }

    // GetOfferRequest/OfferRequestData/PromotionProgramType/Code
    protected void GetOfferRequest_OfferRequestData_PromotionProgramType_Code() {
    }

    // GetOfferRequest/OfferRequestData/PromotionProgramType/Name
    protected void GetOfferRequest_OfferRequestData_PromotionProgramType_Name() {
    }

    // GetOfferRequest/OfferRequestData/PromotionProgramType/ProgramSubType
    protected void GetOfferRequest_OfferRequestData_PromotionProgramType_ProgramSubType() {
        GetOfferRequest_OfferRequestData_PromotionProgramType_ProgramSubType_Code();
        GetOfferRequest_OfferRequestData_PromotionProgramType_ProgramSubType_Name();
    }

    // GetOfferRequest/OfferRequestData/PromotionProgramType/ProgramSubType/Code
    protected void GetOfferRequest_OfferRequestData_PromotionProgramType_ProgramSubType_Code() {
    }

    // GetOfferRequest/OfferRequestData/PromotionProgramType/ProgramSubType/Name
    protected void GetOfferRequest_OfferRequestData_PromotionProgramType_ProgramSubType_Name() {
    }

    // GetOfferRequest/OfferRequestData/PromotionPeriodType
    protected void GetOfferRequest_OfferRequestData_PromotionPeriodType() {
        GetOfferRequest_OfferRequestData_PromotionPeriodType_PromotionPeriodId();
        GetOfferRequest_OfferRequestData_PromotionPeriodType_PromotionPeriodNm();
        GetOfferRequest_OfferRequestData_PromotionPeriodType_PromotionWeekId();
        GetOfferRequest_OfferRequestData_PromotionPeriodType_PromotionStartDt();
        GetOfferRequest_OfferRequestData_PromotionPeriodType_PromotionEndDt();
    }

    // GetOfferRequest/OfferRequestData/PromotionPeriodType/PromotionPeriodId
    protected void GetOfferRequest_OfferRequestData_PromotionPeriodType_PromotionPeriodId() {
    }

    // GetOfferRequest/OfferRequestData/PromotionPeriodType/PromotionPeriodNm
    protected void GetOfferRequest_OfferRequestData_PromotionPeriodType_PromotionPeriodNm() {
    }

    // GetOfferRequest/OfferRequestData/PromotionPeriodType/PromotionWeekId
    protected void GetOfferRequest_OfferRequestData_PromotionPeriodType_PromotionWeekId() {
    }

    // GetOfferRequest/OfferRequestData/PromotionPeriodType/PromotionStartDt
    protected void GetOfferRequest_OfferRequestData_PromotionPeriodType_PromotionStartDt() {
    }

    // GetOfferRequest/OfferRequestData/PromotionPeriodType/PromotionEndDt
    protected void GetOfferRequest_OfferRequestData_PromotionPeriodType_PromotionEndDt() {
    }

    // GetOfferRequest/OfferRequestData/AdvertisementType
    protected void GetOfferRequest_OfferRequestData_AdvertisementType() {
        GetOfferRequest_OfferRequestData_AdvertisementType_Code();
        GetOfferRequest_OfferRequestData_AdvertisementType_Description();
        GetOfferRequest_OfferRequestData_AdvertisementType_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/AdvertisementType/Code
    protected void GetOfferRequest_OfferRequestData_AdvertisementType_Code() {
    }

    // GetOfferRequest/OfferRequestData/AdvertisementType/Description
    protected void GetOfferRequest_OfferRequestData_AdvertisementType_Description() {
    }

    // GetOfferRequest/OfferRequestData/AdvertisementType/ShortDescription
    protected void GetOfferRequest_OfferRequestData_AdvertisementType_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/TriggerId
    protected void GetOfferRequest_OfferRequestData_TriggerId() {
    }

    // GetOfferRequest/OfferRequestData/SavingsValueTxt
    protected void GetOfferRequest_OfferRequestData_SavingsValueTxt() {
    }

    // GetOfferRequest/OfferRequestData/DisclaimerTxt
    protected void GetOfferRequest_OfferRequestData_DisclaimerTxt() {
    }

    // GetOfferRequest/OfferRequestData/ImageId
    protected void GetOfferRequest_OfferRequestData_ImageId() {
    }

    // GetOfferRequest/OfferRequestData/OfferPeriodType
    protected void GetOfferRequest_OfferRequestData_OfferPeriodType() {
        GetOfferRequest_OfferRequestData_OfferPeriodType_DisplayStartDt();
        GetOfferRequest_OfferRequestData_OfferPeriodType_DisplayEndDt();
        GetOfferRequest_OfferRequestData_OfferPeriodType_OfferStartDt();
        GetOfferRequest_OfferRequestData_OfferPeriodType_OfferEndDt();
        GetOfferRequest_OfferRequestData_OfferPeriodType_TestStartDt();
        GetOfferRequest_OfferRequestData_OfferPeriodType_TestEndDt();
    }

    // GetOfferRequest/OfferRequestData/OfferPeriodType/DisplayStartDt
    protected void GetOfferRequest_OfferRequestData_OfferPeriodType_DisplayStartDt() {
    }

    // GetOfferRequest/OfferRequestData/OfferPeriodType/DisplayEndDt
    protected void GetOfferRequest_OfferRequestData_OfferPeriodType_DisplayEndDt() {
    }

    // GetOfferRequest/OfferRequestData/OfferPeriodType/OfferStartDt
    protected void GetOfferRequest_OfferRequestData_OfferPeriodType_OfferStartDt() {
    }

    // GetOfferRequest/OfferRequestData/OfferPeriodType/OfferEndDt
    protected void GetOfferRequest_OfferRequestData_OfferPeriodType_OfferEndDt() {
    }

    // GetOfferRequest/OfferRequestData/OfferPeriodType/TestStartDt
    protected void GetOfferRequest_OfferRequestData_OfferPeriodType_TestStartDt() {
    }

    // GetOfferRequest/OfferRequestData/OfferPeriodType/TestEndDt
    protected void GetOfferRequest_OfferRequestData_OfferPeriodType_TestEndDt() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource() {
        GetOfferRequest_OfferRequestData_OfferRequestSource_SourceSystemId();
        GetOfferRequest_OfferRequestData_OfferRequestSource_ApplicationId();
        GetOfferRequest_OfferRequestData_OfferRequestSource_UpdatedApplicationId();
        GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource/SourceSystemId
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource_SourceSystemId() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource/ApplicationId
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource_ApplicationId() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource/UpdatedApplicationId
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource_UpdatedApplicationId() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource/UserUpdate
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate() {
        GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_UserTypeCd();
        GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_UserId();
        GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_FirstNm();
        GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_LastNm();
        GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_CreateTs();
        GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_UpdateTs();
        GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_TimeZoneCd();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource/UserUpdate/UserTypeCd
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_UserTypeCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource/UserUpdate/UserId
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_UserId() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource/UserUpdate/FirstNm
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_FirstNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource/UserUpdate/LastNm
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_LastNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource/UserUpdate/CreateTs
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_CreateTs() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource/UserUpdate/UpdateTs
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_UpdateTs() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestSource/UserUpdate/TimeZoneCd
    protected void GetOfferRequest_OfferRequestData_OfferRequestSource_UserUpdate_TimeZoneCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferEffectiveDay
    protected void GetOfferRequest_OfferRequestData_OfferEffectiveDay() {
        GetOfferRequest_OfferRequestData_OfferEffectiveDay_DayOfWk();
        GetOfferRequest_OfferRequestData_OfferEffectiveDay_DayOfWkInd();
    }

    // GetOfferRequest/OfferRequestData/OfferEffectiveDay/DayOfWk
    protected void GetOfferRequest_OfferRequestData_OfferEffectiveDay_DayOfWk() {
    }

    // GetOfferRequest/OfferRequestData/OfferEffectiveDay/DayOfWkInd
    protected void GetOfferRequest_OfferRequestData_OfferEffectiveDay_DayOfWkInd() {
    }

    // GetOfferRequest/OfferRequestData/OfferEffectiveTm
    protected void GetOfferRequest_OfferRequestData_OfferEffectiveTm() {
        GetOfferRequest_OfferRequestData_OfferEffectiveTm_StartTm();
        GetOfferRequest_OfferRequestData_OfferEffectiveTm_EndTm();
        GetOfferRequest_OfferRequestData_OfferEffectiveTm_TimeZoneCd();
    }

    // GetOfferRequest/OfferRequestData/OfferEffectiveTm/StartTm
    protected void GetOfferRequest_OfferRequestData_OfferEffectiveTm_StartTm() {
    }

    // GetOfferRequest/OfferRequestData/OfferEffectiveTm/EndTm
    protected void GetOfferRequest_OfferRequestData_OfferEffectiveTm_EndTm() {
    }

    // GetOfferRequest/OfferRequestData/OfferEffectiveTm/TimeZoneCd
    protected void GetOfferRequest_OfferRequestData_OfferEffectiveTm_TimeZoneCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType() {
        GetOfferRequest_OfferRequestData_OfferRestrictionType_LimitQty();
        GetOfferRequest_OfferRequestData_OfferRestrictionType_LimitWt();
        GetOfferRequest_OfferRequestData_OfferRestrictionType_LimitVol();
        GetOfferRequest_OfferRequestData_OfferRestrictionType_UOM();
        GetOfferRequest_OfferRequestData_OfferRestrictionType_LimitAmt();
        GetOfferRequest_OfferRequestData_OfferRestrictionType_RestrictionType();
        GetOfferRequest_OfferRequestData_OfferRestrictionType_UsageLimitTypeTxt();
        GetOfferRequest_OfferRequestData_OfferRestrictionType_UsageLimitPeriodNbr();
        GetOfferRequest_OfferRequestData_OfferRestrictionType_UsageLimitNbr();
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/LimitQty
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_LimitQty() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/LimitWt
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_LimitWt() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/LimitVol
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_LimitVol() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/UOM
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_UOM() {
        GetOfferRequest_OfferRequestData_OfferRestrictionType_UOM_UOMCd();
        GetOfferRequest_OfferRequestData_OfferRestrictionType_UOM_UOMNm();
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/UOM/UOMCd
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_UOM_UOMCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/UOM/UOMNm
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_UOM_UOMNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/LimitAmt
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_LimitAmt() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/RestrictionType
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_RestrictionType() {
        GetOfferRequest_OfferRequestData_OfferRestrictionType_RestrictionType_Code();
        GetOfferRequest_OfferRequestData_OfferRestrictionType_RestrictionType_Description();
        GetOfferRequest_OfferRequestData_OfferRestrictionType_RestrictionType_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/RestrictionType/Code
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_RestrictionType_Code() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/RestrictionType/Description
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_RestrictionType_Description() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/RestrictionType/ShortDescription
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_RestrictionType_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/UsageLimitTypeTxt
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_UsageLimitTypeTxt() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/UsageLimitPeriodNbr
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_UsageLimitPeriodNbr() {
    }

    // GetOfferRequest/OfferRequestData/OfferRestrictionType/UsageLimitNbr
    protected void GetOfferRequest_OfferRequestData_OfferRestrictionType_UsageLimitNbr() {
    }

    // GetOfferRequest/OfferRequestData/RequirementType
    protected void GetOfferRequest_OfferRequestData_RequirementType() {
        GetOfferRequest_OfferRequestData_RequirementType_RequirementTypeCd();
        GetOfferRequest_OfferRequestData_RequirementType_RequiredQty();
        GetOfferRequest_OfferRequestData_RequirementType_RequiredInd();
    }

    // GetOfferRequest/OfferRequestData/RequirementType/RequirementTypeCd
    protected void GetOfferRequest_OfferRequestData_RequirementType_RequirementTypeCd() {
    }

    // GetOfferRequest/OfferRequestData/RequirementType/RequiredQty
    protected void GetOfferRequest_OfferRequestData_RequirementType_RequiredQty() {
    }

    // GetOfferRequest/OfferRequestData/RequirementType/RequiredInd
    protected void GetOfferRequest_OfferRequestData_RequirementType_RequiredInd() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestReferenceId
    protected void GetOfferRequest_OfferRequestData_OfferRequestReferenceId() {
        GetOfferRequest_OfferRequestData_OfferRequestReferenceId_QualifierCd();
    }

    // GetOfferRequest/OfferRequestData/OfferRequestReferenceId/@QualifierCd
    protected void GetOfferRequest_OfferRequestData_OfferRequestReferenceId_QualifierCd() {
    }

    // GetOfferRequest/OfferRequestData/ManufacturerType
    protected void GetOfferRequest_OfferRequestData_ManufacturerType() {
        GetOfferRequest_OfferRequestData_ManufacturerType_IdNbr();
        GetOfferRequest_OfferRequestData_ManufacturerType_IdTxt();
        GetOfferRequest_OfferRequestData_ManufacturerType_Description();
        GetOfferRequest_OfferRequestData_ManufacturerType_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/ManufacturerType/IdNbr
    protected void GetOfferRequest_OfferRequestData_ManufacturerType_IdNbr() {
    }

    // GetOfferRequest/OfferRequestData/ManufacturerType/IdTxt
    protected void GetOfferRequest_OfferRequestData_ManufacturerType_IdTxt() {
    }

    // GetOfferRequest/OfferRequestData/ManufacturerType/Description
    protected void GetOfferRequest_OfferRequestData_ManufacturerType_Description() {
    }

    // GetOfferRequest/OfferRequestData/ManufacturerType/ShortDescription
    protected void GetOfferRequest_OfferRequestData_ManufacturerType_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/AttachmentType
    protected void GetOfferRequest_OfferRequestData_AttachmentType() {
        GetOfferRequest_OfferRequestData_AttachmentType_FileNm();
        GetOfferRequest_OfferRequestData_AttachmentType_LinkURL();
    }

    // GetOfferRequest/OfferRequestData/AttachmentType/FileNm
    protected void GetOfferRequest_OfferRequestData_AttachmentType_FileNm() {
        GetOfferRequest_OfferRequestData_AttachmentType_FileNm_Qualifier();
    }

    // GetOfferRequest/OfferRequestData/AttachmentType/FileNm/@Qualifier
    protected void GetOfferRequest_OfferRequestData_AttachmentType_FileNm_Qualifier() {
    }

    // GetOfferRequest/OfferRequestData/AttachmentType/LinkURL
    protected void GetOfferRequest_OfferRequestData_AttachmentType_LinkURL() {
        GetOfferRequest_OfferRequestData_AttachmentType_LinkURL_Qualifier();
    }

    // GetOfferRequest/OfferRequestData/AttachmentType/LinkURL/@Qualifier
    protected void GetOfferRequest_OfferRequestData_AttachmentType_LinkURL_Qualifier() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestTypeCd
    protected void GetOfferRequest_OfferRequestData_OfferRequestTypeCd() {
    }

    // GetOfferRequest/OfferRequestData/AllocationTypeCd
    protected void GetOfferRequest_OfferRequestData_AllocationTypeCd() {
        GetOfferRequest_OfferRequestData_AllocationTypeCd_Code();
        GetOfferRequest_OfferRequestData_AllocationTypeCd_Description();
        GetOfferRequest_OfferRequestData_AllocationTypeCd_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/AllocationTypeCd/Code
    protected void GetOfferRequest_OfferRequestData_AllocationTypeCd_Code() {
    }

    // GetOfferRequest/OfferRequestData/AllocationTypeCd/Description
    protected void GetOfferRequest_OfferRequestData_AllocationTypeCd_Description() {
    }

    // GetOfferRequest/OfferRequestData/AllocationTypeCd/ShortDescription
    protected void GetOfferRequest_OfferRequestData_AllocationTypeCd_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/BusinessJustificationTxt
    protected void GetOfferRequest_OfferRequestData_BusinessJustificationTxt() {
    }

    // GetOfferRequest/OfferRequestData/OfferRequestCommentTxt
    protected void GetOfferRequest_OfferRequestData_OfferRequestCommentTxt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOfferTypeId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroupVersionId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_DisplayOrderNbr();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProtoType();
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion();
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreTagType();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType();
        GetOfferRequest_OfferRequestData_AttachedOfferType_InstantWinProgramType();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOfferTypeId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOfferTypeId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreGroupVersionId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroupVersionId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/DisplayOrderNbr
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_DisplayOrderNbr() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProtoType
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProtoType() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProtoType_Code();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProtoType_Description();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProtoType_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProtoType/Code
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProtoType_Code() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProtoType/Description
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProtoType_Description() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProtoType/ShortDescription
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProtoType_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreGroup
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupNm();
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupDsc();
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupType();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreGroup/StoreGroupId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreGroup/StoreGroupNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreGroup/StoreGroupDsc
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupDsc() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreGroup/StoreGroupType
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupType() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupType_Code();
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupType_Description();
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupType_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreGroup/StoreGroupType/Code
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupType_Code() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreGroup/StoreGroupType/Description
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupType_Description() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreGroup/StoreGroupType/ShortDescription
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreGroup_StoreGroupType_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupVersionId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupNm();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupDsc();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_DisplayOrderNbr();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ItemQty();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_UOM();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_GiftCardInd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_AnyProductInd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_UniqueItemInd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ConjunctionDsc();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_MinimumPurchaseAmt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_MaximumPurchaseAmt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_InheritedInd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ExcludedProductGroup();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupTier();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupVersionId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupVersionId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupDsc
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupDsc() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/DisplayOrderNbr
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_DisplayOrderNbr() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ItemQty
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ItemQty() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/UOM
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_UOM() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_UOM_UOMCd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_UOM_UOMNm();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_UOM_UOMDsc();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/UOM/UOMCd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_UOM_UOMCd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/UOM/UOMNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_UOM_UOMNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/UOM/UOMDsc
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_UOM_UOMDsc() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/GiftCardInd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_GiftCardInd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/AnyProductInd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_AnyProductInd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/UniqueItemInd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_UniqueItemInd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ConjunctionDsc
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ConjunctionDsc() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/MinimumPurchaseAmt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_MinimumPurchaseAmt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/MaximumPurchaseAmt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_MaximumPurchaseAmt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/InheritedInd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_InheritedInd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ExcludedProductGroup
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ExcludedProductGroup() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ExcludedProductGroup_ProductGroupId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ExcludedProductGroup_ProductGroupNm();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ExcludedProductGroup/ProductGroupId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ExcludedProductGroup_ProductGroupId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ExcludedProductGroup/ProductGroupNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ExcludedProductGroup_ProductGroupNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupTier
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupTier() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupTier_TierLevelId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupTier_TierLevelAmt();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupTier/TierLevelId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupTier_TierLevelId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupTier/TierLevelAmt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupTier_TierLevelAmt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_CorporateItemCd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_UPC();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_StatusReason();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/CorporateItemCd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_CorporateItemCd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/UPC
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_UPC() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_UPC_Qualifier();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_UPC_UPCNbr();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_UPC_UPCTxt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_UPC_UPCDsc();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/UPC/@Qualifier
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_UPC_Qualifier() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/UPC/UPCNbr
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_UPC_UPCNbr() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/UPC/UPCTxt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_UPC_UPCTxt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/UPC/UPCDsc
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_UPC_UPCDsc() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/RepresentativeStatus
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus_StatusTypeCd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus_Description();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus_EffectiveStartTs();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus_EffectiveEndTs();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/RepresentativeStatus/StatusTypeCd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus_StatusTypeCd() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus_StatusTypeCd_Type();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/RepresentativeStatus/StatusTypeCd/@Type
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus_StatusTypeCd_Type() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/RepresentativeStatus/Description
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus_Description() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/RepresentativeStatus/EffectiveStartTs
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus_EffectiveStartTs() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/RepresentativeStatus/EffectiveEndTs
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_RepresentativeStatus_EffectiveEndTs() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/StatusReason
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_StatusReason() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_StatusReason_Code();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_StatusReason_Description();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_StatusReason_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/StatusReason/Code
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_StatusReason_Code() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/StatusReason/Description
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_StatusReason_Description() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/StatusReason/ShortDescription
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_StatusReason_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/ItemOfferPrice
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_ItemOfferPriceAmt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_UOM();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_EffectiveStartTs();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_EffectiveEndTs();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/ItemOfferPrice/ItemOfferPriceAmt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_ItemOfferPriceAmt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/ItemOfferPrice/UOM
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_UOM() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_UOM_UOMCd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_UOM_UOMNm();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/ItemOfferPrice/UOM/UOMCd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_UOM_UOMCd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/ItemOfferPrice/UOM/UOMNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_UOM_UOMNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/ItemOfferPrice/EffectiveStartTs
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_EffectiveStartTs() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/ProductGroup/ProductGroupItemType/ItemOfferPrice/EffectiveEndTs
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_ProductGroup_ProductGroupItemType_ItemOfferPrice_EffectiveEndTs() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_DiscountVersionId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/DiscountVersionId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_DiscountVersionId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountType();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DisplayOrderNbr();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_BenefitValueType();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_BenefitValueQty();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_IncludedProductGroupId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_IncludedProductgroupNm();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_ExcludedProductGroupId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_ExcludedProductGroupNm();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_ChargebackDepartmentId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_ChargebackDepartmentNm();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountType
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountType() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountType_Code();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountType_Description();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountType_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountType/Code
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountType_Code() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountType/Description
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountType_Description() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountType/ShortDescription
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountType_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DisplayOrderNbr
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DisplayOrderNbr() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/BenefitValueType
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_BenefitValueType() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_BenefitValueType_Code();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_BenefitValueType_Description();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_BenefitValueType_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/BenefitValueType/Code
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_BenefitValueType_Code() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/BenefitValueType/Description
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_BenefitValueType_Description() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/BenefitValueType/ShortDescription
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_BenefitValueType_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/BenefitValueQty
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_BenefitValueQty() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/IncludedProductGroupId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_IncludedProductGroupId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/IncludedProductgroupNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_IncludedProductgroupNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/ExcludedProductGroupId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_ExcludedProductGroupId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/ExcludedProductGroupNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_ExcludedProductGroupNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/ChargebackDepartmentId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_ChargebackDepartmentId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/ChargebackDepartmentNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_ChargebackDepartmentNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_TierLevelnbr();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_DiscountAmt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_RewardQty();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_ReceiptTxt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_DiscountUptoQty();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/TierLevelnbr
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_TierLevelnbr() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/DiscountAmt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_DiscountAmt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/LimitType
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_LimitQty();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_LimitWt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_LimitVol();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_UOM();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_LimitAmt();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/LimitType/LimitQty
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_LimitQty() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/LimitType/LimitWt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_LimitWt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/LimitType/LimitVol
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_LimitVol() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/LimitType/UOM
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_UOM() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_UOM_UOMCd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_UOM_UOMNm();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/LimitType/UOM/UOMCd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_UOM_UOMCd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/LimitType/UOM/UOMNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_UOM_UOMNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/LimitType/LimitAmt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_LimitType_LimitAmt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/RewardQty
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_RewardQty() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/ReceiptTxt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_ReceiptTxt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/Discount/DiscountTier/DiscountUptoQty
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_Discount_DiscountTier_DiscountUptoQty() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/AirMileProgram
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram_AirMileProgramId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram_AirMileProgramNm();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram_AirMileTier();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/AirMileProgram/AirMileProgramId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram_AirMileProgramId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/AirMileProgram/AirMileProgramNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram_AirMileProgramNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/AirMileProgram/AirMileTier
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram_AirMileTier() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram_AirMileTier_AirMileTierNm();
        GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram_AirMileTier_AirMilePointQty();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/AirMileProgram/AirMileTier/AirMileTierNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram_AirMileTier_AirMileTierNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/ProductGroupVersion/DiscountVersion/AirMileProgram/AirMileTier/AirMilePointQty
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_ProductGroupVersion_DiscountVersion_AirMileProgram_AirMileTier_AirMilePointQty() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreTagType
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreTagType() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreTagType_TagNbr();
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreTagType_TagAmt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreTagType_LoyaltyPgmTagInd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_StoreTagType_TagDsc();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreTagType/TagNbr
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreTagType_TagNbr() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreTagType/TagAmt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreTagType_TagAmt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreTagType/LoyaltyPgmTagInd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreTagType_LoyaltyPgmTagInd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/StoreTagType/TagDsc
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_StoreTagType_TagDsc() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisplayImage();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_HeadlineTxt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_HeadlineSubTxt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_OfferDsc();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_OfferDetail();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_PriceInfoTxt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_ItemQty();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_UOM();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_UsageLimitTypeTxt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisclaimerTxt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisplayStartDt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisplayEndDt();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_SpecialEventType();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_CustomerFriendlyCategory();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_ShoppingListCategory();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_PODCategory();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/DisplayImage
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisplayImage() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisplayImage_ImageTypeCd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisplayImage_ImageId();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/DisplayImage/ImageTypeCd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisplayImage_ImageTypeCd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/DisplayImage/ImageId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisplayImage_ImageId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/HeadlineTxt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_HeadlineTxt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/HeadlineSubTxt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_HeadlineSubTxt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/OfferDsc
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_OfferDsc() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/OfferDetail
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_OfferDetail() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_OfferDetail_Code();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_OfferDetail_Description();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_OfferDetail_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/OfferDetail/Code
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_OfferDetail_Code() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/OfferDetail/Description
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_OfferDetail_Description() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/OfferDetail/ShortDescription
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_OfferDetail_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/PriceInfoTxt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_PriceInfoTxt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/ItemQty
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_ItemQty() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/UOM
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_UOM() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_UOM_UOMCd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_UOM_UOMNm();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/UOM/UOMCd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_UOM_UOMCd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/UOM/UOMNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_UOM_UOMNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/UsageLimitTypeTxt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_UsageLimitTypeTxt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/DisclaimerTxt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisclaimerTxt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/DisplayStartDt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisplayStartDt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/DisplayEndDt
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_DisplayEndDt() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/SpecialEventType
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_SpecialEventType() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_SpecialEventType_EventId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_SpecialEventType_EventNm();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/SpecialEventType/EventId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_SpecialEventType_EventId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/SpecialEventType/EventNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_SpecialEventType_EventNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/CustomerFriendlyCategory
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_CustomerFriendlyCategory() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_CustomerFriendlyCategory_Code();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_CustomerFriendlyCategory_Description();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_CustomerFriendlyCategory_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/CustomerFriendlyCategory/Code
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_CustomerFriendlyCategory_Code() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/CustomerFriendlyCategory/Description
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_CustomerFriendlyCategory_Description() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/CustomerFriendlyCategory/ShortDescription
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_CustomerFriendlyCategory_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/ShoppingListCategory
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_ShoppingListCategory() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_ShoppingListCategory_Code();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_ShoppingListCategory_Description();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_ShoppingListCategory_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/ShoppingListCategory/Code
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_ShoppingListCategory_Code() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/ShoppingListCategory/Description
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_ShoppingListCategory_Description() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/ShoppingListCategory/ShortDescription
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_ShoppingListCategory_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/PODCategory
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_PODCategory() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_PODCategory_Code();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_PODCategory_Description();
        GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_PODCategory_ShortDescription();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/PODCategory/Code
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_PODCategory_Code() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/PODCategory/Description
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_PODCategory_Description() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/PODDetailType/PODCategory/ShortDescription
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_PODDetailType_PODCategory_ShortDescription() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/InstantWinProgramType
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_InstantWinProgramType() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_InstantWinProgramType_InstantWinProgramId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_InstantWinProgramType_InstantWinVersionId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_InstantWinProgramType_PrizeItemQty();
        GetOfferRequest_OfferRequestData_AttachedOfferType_InstantWinProgramType_FrequencyDsc();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/InstantWinProgramType/InstantWinProgramId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_InstantWinProgramType_InstantWinProgramId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/InstantWinProgramType/InstantWinVersionId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_InstantWinProgramType_InstantWinVersionId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/InstantWinProgramType/PrizeItemQty
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_InstantWinProgramType_PrizeItemQty() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/InstantWinProgramType/FrequencyDsc
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_InstantWinProgramType_FrequencyDsc() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_StoreGroupVersionId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_ProductGroupVersionId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_DiscountVersionId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_InstantWinVersionId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_DiscountId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_OfferId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_ReferenceOfferId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_Status();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_AppliedProgram();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_DistinctId();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_OfferRankNbr();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/StoreGroupVersionId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_StoreGroupVersionId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/ProductGroupVersionId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_ProductGroupVersionId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/DiscountVersionId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_DiscountVersionId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/InstantWinVersionId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_InstantWinVersionId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/DiscountId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_DiscountId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/OfferId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_OfferId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/ReferenceOfferId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_ReferenceOfferId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/Status
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_Status() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_Status_StatusTypeCd();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_Status_Description();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_Status_EffectiveDtTm();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/Status/StatusTypeCd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_Status_StatusTypeCd() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_Status_StatusTypeCd_Type();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/Status/StatusTypeCd/@Type
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_Status_StatusTypeCd_Type() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/Status/Description
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_Status_Description() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/Status/EffectiveDtTm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_Status_EffectiveDtTm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/AppliedProgram
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_AppliedProgram() {
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_AppliedProgram_ProgramNm();
        GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_AppliedProgram_AppliedInd();
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/AppliedProgram/ProgramNm
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_AppliedProgram_ProgramNm() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/AppliedProgram/AppliedInd
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_AppliedProgram_AppliedInd() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/DistinctId
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_DistinctId() {
    }

    // GetOfferRequest/OfferRequestData/AttachedOfferType/AttachedOffer/OfferRankNbr
    protected void GetOfferRequest_OfferRequestData_AttachedOfferType_AttachedOffer_OfferRankNbr() {
    }

    // GetOfferRequest/OfferRequestData/VersionQty
    protected void GetOfferRequest_OfferRequestData_VersionQty() {
    }

    // GetOfferRequest/OfferRequestData/TierQty
    protected void GetOfferRequest_OfferRequestData_TierQty() {
    }

    // GetOfferRequest/OfferRequestData/ProductQty
    protected void GetOfferRequest_OfferRequestData_ProductQty() {
    }

    // GetOfferRequest/OfferRequestData/StoreGroupQty
    protected void GetOfferRequest_OfferRequestData_StoreGroupQty() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType() {
        GetOfferRequest_OfferRequestData_DeletedOfferType_StoreGroupVersionId();
        GetOfferRequest_OfferRequestData_DeletedOfferType_ProductGroupVersionId();
        GetOfferRequest_OfferRequestData_DeletedOfferType_DiscountVersionId();
        GetOfferRequest_OfferRequestData_DeletedOfferType_InstantWinVersionId();
        GetOfferRequest_OfferRequestData_DeletedOfferType_DiscountId();
        GetOfferRequest_OfferRequestData_DeletedOfferType_OfferId();
        GetOfferRequest_OfferRequestData_DeletedOfferType_ReferenceOfferId();
        GetOfferRequest_OfferRequestData_DeletedOfferType_Status();
        GetOfferRequest_OfferRequestData_DeletedOfferType_AppliedProgram();
        GetOfferRequest_OfferRequestData_DeletedOfferType_DistinctId();
        GetOfferRequest_OfferRequestData_DeletedOfferType_OfferRankNbr();
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/StoreGroupVersionId
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_StoreGroupVersionId() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/ProductGroupVersionId
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_ProductGroupVersionId() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/DiscountVersionId
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_DiscountVersionId() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/InstantWinVersionId
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_InstantWinVersionId() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/DiscountId
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_DiscountId() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/OfferId
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_OfferId() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/ReferenceOfferId
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_ReferenceOfferId() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/Status
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_Status() {
        GetOfferRequest_OfferRequestData_DeletedOfferType_Status_StatusTypeCd();
        GetOfferRequest_OfferRequestData_DeletedOfferType_Status_Description();
        GetOfferRequest_OfferRequestData_DeletedOfferType_Status_EffectiveDtTm();
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/Status/StatusTypeCd
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_Status_StatusTypeCd() {
        GetOfferRequest_OfferRequestData_DeletedOfferType_Status_StatusTypeCd_Type();
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/Status/StatusTypeCd/@Type
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_Status_StatusTypeCd_Type() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/Status/Description
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_Status_Description() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/Status/EffectiveDtTm
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_Status_EffectiveDtTm() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/AppliedProgram
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_AppliedProgram() {
        GetOfferRequest_OfferRequestData_DeletedOfferType_AppliedProgram_ProgramNm();
        GetOfferRequest_OfferRequestData_DeletedOfferType_AppliedProgram_AppliedInd();
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/AppliedProgram/ProgramNm
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_AppliedProgram_ProgramNm() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/AppliedProgram/AppliedInd
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_AppliedProgram_AppliedInd() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/DistinctId
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_DistinctId() {
    }

    // GetOfferRequest/OfferRequestData/DeletedOfferType/OfferRankNbr
    protected void GetOfferRequest_OfferRequestData_DeletedOfferType_OfferRankNbr() {
    }

    // GetOfferRequest/OfferRequestData/ChargeBackDepartment
    protected void GetOfferRequest_OfferRequestData_ChargeBackDepartment() {
        GetOfferRequest_OfferRequestData_ChargeBackDepartment_DepartmentId();
        GetOfferRequest_OfferRequestData_ChargeBackDepartment_DepartmentNm();
    }

    // GetOfferRequest/OfferRequestData/ChargeBackDepartment/DepartmentId
    protected void GetOfferRequest_OfferRequestData_ChargeBackDepartment_DepartmentId() {
    }

    // GetOfferRequest/OfferRequestData/ChargeBackDepartment/DepartmentNm
    protected void GetOfferRequest_OfferRequestData_ChargeBackDepartment_DepartmentNm() {
    }

    // GetOfferRequest/OfferRequestData/OfferReviewChecklist
    protected void GetOfferRequest_OfferRequestData_OfferReviewChecklist() {
        GetOfferRequest_OfferRequestData_OfferReviewChecklist_ReviewChecklistTypeCd();
        GetOfferRequest_OfferRequestData_OfferReviewChecklist_ReviewChecklistInd();
    }

    // GetOfferRequest/OfferRequestData/OfferReviewChecklist/ReviewChecklistTypeCd
    protected void GetOfferRequest_OfferRequestData_OfferReviewChecklist_ReviewChecklistTypeCd() {
    }

    // GetOfferRequest/OfferRequestData/OfferReviewChecklist/ReviewChecklistInd
    protected void GetOfferRequest_OfferRequestData_OfferReviewChecklist_ReviewChecklistInd() {
    }
}