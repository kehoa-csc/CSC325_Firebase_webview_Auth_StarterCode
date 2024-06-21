# PLEASE NOTE:
There are some things for this to work that may only be functional on my specific instance.
If it's not working:
* Set storage bucket in firestorecontext to your Firebase project name.
* In uploadPicButton() method on AccessFBView.java, note `Bucket.BlobWriteOption.userProject("csc325-78793"));` and change my project name quotes to your project name.
* If a 403 permissions error appears, go to Project Settings in Firebase, Users and Permissions, then Service Accounts. The account in the Stack trace should be there, so give it Owner permissions.
