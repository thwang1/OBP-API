package code.branches

import code.branches.Branches.{Branch, DataLicense, BranchData}
import code.model.{BranchId, BankId}
import net.liftweb.common.Logger
import net.liftweb.util.SimpleInjector

object Branches extends SimpleInjector {

  case class BranchId(value : String)
  case class BranchData(branches : List[Branch], license : DataLicense)

  trait DataLicense {
    def name : String
    def url : String
  }

  trait Branch {
    def branchId : BranchId
    def name : String
    def address : Address
  }

  trait Address {
    def line1 : String
    def line2 : String
    def line3 : String
    def line4 : String
    def line5 : String
    def postCode : String
    //ISO_3166-1_alpha-2
    def countryCode : String
  }

  val branchesProvider = new Inject(buildOne _) {}

  def buildOne: BranchesProvider = MappedBranchesProvider

}

trait BranchesProvider {

  private val logger = Logger(classOf[BranchesProvider])

  final def getBranches(bank : BankId) : Option[BranchData] = {
    branchDataLicense(bank) match {
      case Some(license) =>
        Some(BranchData(branchData(bank), license))
      case None => {
        logger.info(s"No branch data license found for bank ${bank.value}")
        None
      }
    }
  }

  // TODO work in progress. Add singular BranchData
  final def getBranch(bank : BankId, branch : BranchId) : Option[BranchData] = {
    branchDataLicense(bank) match {
      case Some(license) =>
        Some(BranchData(branchData(bank), license))
      case None => {
        logger.info(s"No branch data license found for bank ${bank.value}")
        None
      }
    }
  }







  protected def branchData(bank : BankId) : List[Branch]
  protected def branchDataLicense(bank : BankId) : Option[DataLicense]
}


