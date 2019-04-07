using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;
namespace Collectfour
{
    #region Registration
    public class Registration
    {
        #region Member Variables
        protected string _username;
        protected string _password;
        #endregion
        #region Constructors
        public Registration() { }
        public Registration(string username, string password)
        {
            this._username=username;
            this._password=password;
        }
        #endregion
        #region Public Properties
        public virtual string Username
        {
            get {return _username;}
            set {_username=value;}
        }
        public virtual string Password
        {
            get {return _password;}
            set {_password=value;}
        }
        #endregion
    }
    #endregion
}